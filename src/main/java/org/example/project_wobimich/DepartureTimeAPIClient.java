package org.example.project_wobimich;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class DepartureTimeAPIClient {
    private static final String HOST = "www.wienerlinien.at";
    private static final int PORT = 443;
    private String path;
    private String divaID; //for station that has lines assigned to it
    private String stopID; //for station point where lines are departure

    public DepartureTimeAPIClient(String divaID) {
        this.divaID = divaID;
        this.path ="/ogd_realtime/monitor?diva=" + divaID;
    }

    //send a request to the API
    //get request and return it as a string
    /*
    The Wiener Linien OGD Realtime API can only be accessed using HTTPS.
    A normal socket uses HTTP on port 80, so it receives a 302 redirect.
    A raw socket cannot perform the SSL/TLS handshake, so it cannot follow the redirect to HTTPS.
    With SSLSocketFactory, I create an SSLSocket that performs the TLS handshake and establishes a proper HTTPS connection on port 443
     */
    public String fetchAPIResponse() {
        StringBuilder response = new StringBuilder();
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try (SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT)) {
            socket.startHandshake();

            //send request
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET " + this.path + " HTTP/1.1");
            out.println("Host: " + HOST);
            out.println("Connection: close");
            out.println();
            out.flush();

            //read response
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            boolean isBody = false;

            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    isBody = true;
                    continue;
                }
                if (isBody) {
                    response.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    //response of API request is transformed to JSON-format
    //set given UserAddress-object with longitude and latitude
    public JsonNode parseAPIResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        //JsonNode filteredResponse = mapper.createObjectNode();
        ArrayNode filteredResponse = mapper.createArrayNode(); // Array für alle Linien

        try {
            JsonNode rootNode = mapper.readTree(response);

            //only important field of response ==> mapping
            //fields need to be inserted to an object
            if (rootNode.has("data") && !rootNode.get("data").isEmpty()) {
                JsonNode dataArray = rootNode.get("data");

                for (JsonNode data : dataArray) {
                    if (data.has("monitors") && data.get("monitors").isArray()) {
                        for (JsonNode monitor : data.get("monitors")) {
                            if (monitor.has("lines") && monitor.get("lines").isArray()) {
                                for (JsonNode line : monitor.get("lines")) {
                                    ObjectNode lineNode = mapper.createObjectNode();
                                    lineNode.put("lineId", line.get("lineId").asText());
                                    lineNode.put("lineName", line.get("name").asText());
                                    lineNode.put("lineDirection", line.get("towards").asText());
                                    lineNode.put("lineBarrierereFree", line.get("barrierFree").asText());
                                    lineNode.put("realtimeSupported", line.get("realtimeSupported").asText());
                                    lineNode.put("lineType", line.get("type").asText());
                                    lineNode.put("lineID", line.get("lineId").asText());
                                    filteredResponse.add(lineNode);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredResponse;
    }




}

