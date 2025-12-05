package org.example.project_wobimich.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.dto.AddressDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*
This class and its methods are used to request to the WEB-API of data.wien.gv.at and get the response about a specific address
such as longitude,latitude, district, district name and so on...
 */
public class AddressAPIClient {
    private static final String HOST = "www.data.wien.gv.at";
    private static final int PORT = 80;
    private String path;
    private String streetName;
    private String streetNumber;
    private String address;

    public AddressAPIClient(String streetName, String streetNumber) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.address = URLEncoder.encode(this.streetName + " " + this.streetNumber, StandardCharsets.UTF_8);
        this.path = "/daten/OGDAddressService.svc/GetAddressInfo?Address=" + this.address + "&crs=EPSG:4326";
    }

    //send a request to the API
    //get request and return it as a string
    public String fetchAPIResponse() {
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

    public AddressDTO parseAPIResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            AddressDTO.ApiResponse api = mapper.readValue(response, AddressDTO.ApiResponse.class);
            AddressDTO.Feature feature = api.features.getFirst();

            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreetName(feature.properties.StreetName);
            addressDTO.setStreetNumber(feature.properties.StreetNumber);
            addressDTO.setPostalCode(feature.properties.PostalCode);
            addressDTO.setCity(feature.properties.Municipality);
            addressDTO.setLongitude(feature.geometry.coordinates.get(0));
            addressDTO.setLatitude(feature.geometry.coordinates.get(1));

            return addressDTO;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}