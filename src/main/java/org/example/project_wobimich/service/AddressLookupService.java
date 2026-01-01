package org.example.project_wobimich.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.example.project_wobimich.StationUtils;
import org.example.project_wobimich.api.AddressAPIClient;
import org.example.project_wobimich.dto.AddressDTO;
import org.example.project_wobimich.model.Location;
import org.example.project_wobimich.model.Station;

import java.util.ArrayList;
import java.util.Comparator;


public class AddressLookupService extends Service<ArrayList<Station>> {
    private String userInputLocation;

    public AddressLookupService(String userLocation) {
        this.userInputLocation = userLocation;
    }


    @Override
    protected Task<ArrayList<Station>> createTask() {
        return new Task<>() {
            @Override
            protected ArrayList<Station> call() throws Exception {
                AddressAPIClient addressAPIClient = new AddressAPIClient(userInputLocation);
                String apiResponse = addressAPIClient.fetchAPIResponse();
                AddressDTO addressDTO = addressAPIClient.parseAPIResponse(apiResponse);
                Location location = addressDTO.mapToUserLocation();

                ArrayList<Station> stations = StationUtils.listStationsByDistanceFrom(location);
                StationUtils.sortAscending(stations, Comparator.comparing(Station::getDistance));
                return StationUtils.closestStationToLocation(stations);
            }
        };
    }
    
}

