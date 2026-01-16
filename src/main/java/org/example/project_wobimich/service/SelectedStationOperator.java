package org.example.project_wobimich.service;

import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.Station;
import org.example.project_wobimich.model.LineStation;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.ArrayList;


public class SelectedStationOperator {

    private final List<Station> stations;
    private final OutputRightBox outputBox;


    public SelectedStationOperator(List<Station> stations, OutputRightBox outputBox) {
        this.stations = stations;
        this.outputBox = outputBox;
    }

    public void handleSelection(String stationName) {
        Station station = findStationByName(stationName);

        if (station == null) {
            outputBox.showMessage("Keine gültige Station gewählt");
            return;
        }
        try {
            RealTimeMonitorAPIClient client = new RealTimeMonitorAPIClient(station.getId());
            String json = client.fetchAPIResponse();
            List<RealTimeMonitorDTO> dtos = client.parseAPIResponse(json);
            List<LineStation> lines = new ArrayList<>();
            for (RealTimeMonitorDTO dto : dtos) {
                LineStation line = dto.mapToLine();
                line.setCountdownMinutes(calculateCountdownMinutes(line.getDepartureTime()));
                lines.add(line);
            }
            station.setLines(lines);
            outputBox.showLines(station.getName(), lines);
        } catch (Exception e) {
            outputBox.showMessage("Fehler beim Laden der Linien");
        }
    }

    private Station findStationByName(String name) {
        for (Station s : stations) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    private List<Integer> calculateCountdownMinutes(List<String> depatureTimes) {
        List<Integer> countdown = new ArrayList<>();
        if (depatureTimes == null) return countdown;
        for (String t : depatureTimes) {
            countdown.add(minutesUntil(t));
        }
        return countdown;
    }

    private int minutesUntil(String departureTimePlanned) {
        OffsetDateTime departure = parseWienerLinienTime(departureTimePlanned);
        if (departure == null) return 0;

        long minutes = Duration.between(OffsetDateTime.now(), departure).toMinutes();
        return (int) Math.max(minutes, 0);
    }

    private OffsetDateTime parseWienerLinienTime(String t) {
        if (t == null || t.isBlank()) return null;

        // WL liefert oft ...+0100 oder ...-0530 -> mache daraus ...+01:00 / ...-05:30
        if (t.matches(".*[+-]\\d{4}$")) {
            t = t.substring(0, t.length() - 2) + ":" + t.substring(t.length() - 2);
        }
        return OffsetDateTime.parse(t);
    }
}
