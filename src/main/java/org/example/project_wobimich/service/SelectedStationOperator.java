package org.example.project_wobimich.service;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.model.LineStation;

import java.util.List;
import java.util.ArrayList;


public class SelectedStationOperator {

    private final List<Station> stations;
    private final VBox targetBox;

    public SelectedStationOperator(List<Station> stations, VBox targetBox) {
        this.stations = stations;
        this.targetBox = targetBox;
    }

    public void handleSelection(String stationName) {
        Station station = findStationByName(stationName);

        if (station == null) {
            showMessage("Keine gültige Station gewählt");
        }

            RealTimeMonitorAPIClient client = new RealTimeMonitorAPIClient(station.getId());
            String json = client.fetchAPIResponse();
            List<RealTimeMonitorDTO> dtos = client.parseAPIResponse(json);

            List<LineStation> lines = new ArrayList<>();
            for (RealTimeMonitorDTO dto : dtos) {
                lines.add(dto.mapToLine());
            }
//
//            if (dtos == null) {
//                showMessage("Keine Realtime-Daten verfügbar");
//                return;
//            }
//            else-if (dtos.getData() == null) {
//                showMessage("Realtime-Daten fehlerhaft");
//                return;
//            }

            station.setLines(lines);
            showLines(lines, station.getName());
        }


    private Station findStationByName(String name) {
        for (Station s : stations) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    private void showLines(List<LineStation> lines, String stationName) {
        targetBox.getChildren().clear();

        if (lines == null) {
            showMessage("Keine gültige Linie verfügbar");
            return;
        }
        else if (lines.isEmpty()) {
            showMessage("Linien nicht verfügbar");
            return;
        }

        Label header = new Label(stationName);
            header.setStyle("-fx-font-weight: bold");
        targetBox.getChildren().add(header);

        for (LineStation line : lines) {
            Label lineLabel = new Label(formatLine(line));
            targetBox.getChildren().add(lineLabel);
        }



    }
    private String formatLine(LineStation line) {
        return "Linie " + line.getId()
                + " " + line.getName() + " nach " + line.getDirection();
    }



    private void showMessage(String message) {
        targetBox.getChildren().clear();
        targetBox.getChildren().add(new Label(message));
    }
}
