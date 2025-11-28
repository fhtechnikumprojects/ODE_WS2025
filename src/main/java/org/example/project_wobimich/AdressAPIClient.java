package org.example.project_wobimich;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AdressAPIClient {
    private static final String HOST = "data.wien.gv.at";
    private static final int PORT = 80;
    private String guiInputStreetName;
    private String guiInputStreetNumber;
    private String address;

    public AdressAPIClient(String guiInputStreetName, String guiInputStreetNumber) {
        this.guiInputStreetName = guiInputStreetName;
        this.guiInputStreetNumber = guiInputStreetNumber;
        this.address = URLEncoder.encode(guiInputStreetName + " " + guiInputStreetNumber, StandardCharsets.UTF_8);
    }

    public String fetchAPIResponse() {
        String path = "/daten/OGDAddressService.svc/GetAddressInfo?Address=" + this.address + "&crs=EPSG:4326";
        StringBuilder response = new StringBuilder();

        try (Socket socket = new Socket(HOST, PORT)) {
            //send request
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET " + path + " HTTP/1.1");
            out.println("Host: " + HOST);
            out.println("Connection: close");
            out.println();

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

    public JsonNode parseAPIResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode filteredResponse = mapper.createObjectNode();

        try {
            JsonNode rootNode = mapper.readTree(response);

            //only important field of response ==> mapping
            //fields need to be inserted to an object
            if (rootNode.has("features") && !rootNode.get("features").isEmpty()) {
                JsonNode feature = rootNode.get("features").get(0);
                JsonNode geometry = feature.get("geometry");
                JsonNode properties = feature.get("properties");

                double longitude = geometry.get("coordinates").get(0).asDouble();
                double latitude = geometry.get("coordinates").get(1).asDouble();
                String streetName = properties.get("StreetName").asText();
                String streetNumber = properties.get("StreetNumber").asText();
                String zipcode = properties.get("PostalCode").asText();
                String city = properties.get("Municipality").asText();

                filteredResponse = mapper.createObjectNode()
                        .put("StreetName", streetName)
                        .put("StreetNumber", streetNumber)
                        .put("PostalCode", zipcode)
                        .put("City", city)
                        .put("Longitude", longitude)
                        .put("Latitude", latitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredResponse;
    }












}