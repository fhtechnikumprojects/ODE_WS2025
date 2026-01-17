package org.example.project_wobimich.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.LineStation;


import java.util.ArrayList;
import java.util.List;

public class LineLookupService extends Service<List<LineStation>> {
    private String stationID;

    public LineLookupService(String stationID) {
        this.stationID = stationID;
    }

    @Override
    protected Task<List<LineStation>> createTask() {
        return new Task<>() {
            @Override
            protected List<LineStation> call() throws Exception {
                RealTimeMonitorAPIClient realTimeMonitorAPIClient = new RealTimeMonitorAPIClient(stationID);
                String apiResponse = realTimeMonitorAPIClient.fetchAPIResponse();

                List<RealTimeMonitorDTO> listRealTimeMonitor = realTimeMonitorAPIClient.parseAPIResponse(apiResponse);

                List<LineStation> lineStations = new ArrayList<>();
                for (RealTimeMonitorDTO RTM : listRealTimeMonitor) {
                    lineStations.add(new LineStation(
                            RTM.getLineID(),
                            RTM.getLineName(),
                            RTM.getDirection(),
                            RTM.getTypeOfTransportation(),
                            RTM.isBarrierFree(),
                            RTM.isRealTimeSupported(),
                            RTM.getDepartureTime()
                    ));
                }

                return lineStations;
            }
        };
    }
}
