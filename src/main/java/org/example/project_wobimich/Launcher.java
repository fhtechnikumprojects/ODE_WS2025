package org.example.project_wobimich;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import java.io.*;
import java.net.Socket;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        UserAddress userAddress = new UserAddress("Jägerstraße", "10");
        AdressAPIClient address = new AdressAPIClient(userAddress.getStreetName(), userAddress.getStreetNumber());
        System.out.println(address.parseAPIResponse(address.fetchAPIResponse()).toPrettyString());


        DepartureTimeAPIClient departureTimeAPIClient = new DepartureTimeAPIClient("60200657");
        String response = departureTimeAPIClient.fetchAPIResponse();
        System.out.println(response);
        //System.out.println(departureTimeAPIClient.parseAPIResponse(response).toPrettyString());

    }
}
