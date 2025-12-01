package org.example.project_wobimich;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;


public class RealTimeMonitorAPIClient {
    private static final String HOST = "www.wienerlinien.at";
    private static final int PORT = 443;
    private String path;
    private String divaID; //for station that has lines assigned to it
    private String stopID; //for station point where lines are departure

    public RealTimeMonitorAPIClient(String divaID) {
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
                    // Skip chunk size lines  ==> start at body
                    if (line.matches("^[0-9a-fA-F]+$")) {
                        continue;
                    }
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
    public ArrayNode parseAPIResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode filteredResponseArray = mapper.createArrayNode();

        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode monitors = rootNode.get("data").get("monitors");

            if (monitors.isArray() && !monitors.isEmpty()) {
                for (JsonNode mon : monitors) {
                    JsonNode lines = mon.get("lines");
                    if (lines.isArray() && !lines.isEmpty()) {
                        for (JsonNode line : lines) {
                            String name = line.get("name").asText();
                            String direction = line.get("towards").asText();
                            boolean barrierFree = line.get("barrierFree").asBoolean();
                            boolean realtimeSupported = line.get("realtimeSupported").asBoolean();
                            String typeOfTransportation = line.get("type").asText();
                            String lineId = line.get("lineId").asText();

                            JsonNode currentNode = mapper.createObjectNode()
                                    .put("lineId",lineId)
                                    .put("name",name)
                                    .put("direction",direction)
                                    .put("barrierFree",barrierFree)
                                    .put("realtimeSupported",realtimeSupported)
                                    .put("typeOfTransportation",typeOfTransportation);

                            filteredResponseArray.add(currentNode);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredResponseArray;
    }




}

