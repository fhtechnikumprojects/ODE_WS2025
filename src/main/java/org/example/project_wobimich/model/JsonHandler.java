package org.example.project_wobimich.model;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.Address;
import org.example.project_wobimich.Location;

import java.io.File;
import java.io.IOException;

public class JsonIterator {

    public JsonIterator(){};

    ObjectMapper mapper = new ObjectMapper();

    public void loadJsonFile(File filePath, Location location) {
        try(MappingIterator<Address> iter = mapper.readerFor(Address.class).readValues(filePath)) {

            while(iter.hasNext()) {
                Address jsonAddress = iter.next();
                if (jsonAddress.getStreet().equals(location.getStreetName()) && jsonAddress.getNumber().equals(location.getStreetNumber())) {
                    location.setLongitude(jsonAddress.getLongitude());
                    location.setLatitude(jsonAddress.getLatitude());

                    System.out.println("Treffer gefunden:");
                    System.out.println("Strasse: " + location.getStreetName());
                    System.out.println("Hausnummer: " + location.getStreetNumber());
                    System.out.println("Longitude: " + location.getLongitude());
                    System.out.println("Latitude: " + location.getLatitude());
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
