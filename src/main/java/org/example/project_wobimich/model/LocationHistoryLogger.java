package org.example.project_wobimich.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocationHistoryLogger {

    private static final File FILE = new File("LocationHistory.json");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static synchronized void logLocation(String location){
        try{
            List<LocationLogEntry> entries = new ArrayList<>();

            if(FILE.exists()){
                entries = MAPPER.readValue(FILE, new TypeReference<List<LocationLogEntry>>() {});
            }
            entries.add(new LocationLogEntry(location));
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE, entries);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
