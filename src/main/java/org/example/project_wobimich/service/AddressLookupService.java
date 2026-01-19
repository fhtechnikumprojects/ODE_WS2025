package org.example.project_wobimich.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.example.project_wobimich.utils.StationUtils;
import org.example.project_wobimich.api.AddressAPIClient;
import org.example.project_wobimich.dto.AddressDTO;
import org.example.project_wobimich.model.Location;
import org.example.project_wobimich.model.Station;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * JavaFX background service that resolves a user-entered address
 * into a list of nearby stations ordered by distance.
 *
 * Runs asynchronously to avoid blocking the JavaFX UI thread.
 */
public class AddressLookupService extends Service<ArrayList<Station>> {
    private String addressQuery;

    public AddressLookupService(String userLocation) {
        this.addressQuery = userLocation;
    }

    /**
     * Creates the background task executed by the JavaFX Service.
     */
    @Override
    protected Task<ArrayList<Station>> createTask() {
        return new Task<>() {
            @Override
            protected ArrayList<Station> call() throws Exception {
                AddressAPIClient addressAPIClient = new AddressAPIClient(addressQuery);
                String apiResponse = addressAPIClient.fetchAPIResponse();
                AddressDTO addressDTO = addressAPIClient.parseAPIResponse(apiResponse);
                Location location = addressDTO.mapToUserLocation();

                ArrayList<Station> stations = StationUtils.getStationsSortedByDistanceFrom(location);

                StationUtils.sortAscending(stations, Comparator.comparing(Station::getDistance));
                return StationUtils.getClosestStations(stations);
            }
        };
    }
    
}

