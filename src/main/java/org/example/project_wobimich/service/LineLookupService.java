package org.example.project_wobimich.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.example.project_wobimich.api.ApiException;
import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.LineStation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX background service that loads real-time departure
 * information for a given station.
 *
 * Executes API calls asynchronously to keep the UI responsive.
 */
public class LineLookupService extends Service<List<LineStation>> {
    private String stationID;

    public LineLookupService(String stationID) {
        this.stationID = stationID;
    }

    /**
     * Creates the background task executed by the JavaFX Service.
     */
    @Override
    protected Task<List<LineStation>> createTask() {
        return new Task<>() {
            @Override
            protected List<LineStation> call() throws ApiException {
                RealTimeMonitorAPIClient realTimeMonitorAPIClient = new RealTimeMonitorAPIClient(stationID);

                try {
                    String apiResponse = realTimeMonitorAPIClient.fetchAPIResponse();

                    List<RealTimeMonitorDTO> realTimeMonitorList = realTimeMonitorAPIClient.parseAPIResponse(apiResponse);

                    List<LineStation> lineStations = new ArrayList<>();
                    for (RealTimeMonitorDTO RTM : realTimeMonitorList) {
                        lineStations.add(RTM.mapToLine());
                    }

                    return lineStations;
                } catch (IOException e) {
                    throw new ApiException("API request f ailed!",e);
                }
            }
        };
    }
}
