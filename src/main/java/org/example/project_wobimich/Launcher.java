package org.example.project_wobimich;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import java.io.*;
import java.net.Socket;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        AdressAPIClient address = new AdressAPIClient("Kierling Banhof", "2");
        System.out.println(address.fetchAPIResponse());
        System.out.println(address.parseAPIResponse(address.fetchAPIResponse()).toPrettyString());




    }
}
