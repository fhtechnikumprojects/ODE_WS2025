package org.example.project_wobimich;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import org.example.project_wobimich.model.JsonIterator;

import java.io.File;

public class Launcher {
    public static void main(String[] args) throws Exception {
        //Application.launch(HelloApplication.class, args);

        String street = "Neuer Markt";
        String number = "3";
        Location location = new Location(street, number);

        File filePath = new File("D:/Users/Kharim/ODE_WS25/src/main/resources/org/example/project_wobimich/data/ADRESSENOGD_1010_filtered.json");

        JsonIterator iter = new JsonIterator();
        iter.loadJsonFile(filePath,location);

    }
}
