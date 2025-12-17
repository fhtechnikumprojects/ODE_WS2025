package org.example.project_wobimich.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.project_wobimich.model.LineStation;

import java.util.List;

/**
 * Data Transfer Object for real-time transport line monitoring.
 * <p>
 * Contains information about line ID, line name, direction, type of transportation,
 * barrier-free accessibility, real-time support, and departure times.
 */
public class RealTimeMonitorDTO {
    private String lineID;
    private String lineName;
    private String direction;
    private String typeOfTransportation;
    private boolean barrierFree;
    private boolean realTimeSupported;
    private List<String> departureTime;

    public String getLineID() { return this.lineID; }
    public void setLineID(String lineID) { this.lineID = lineID;
    }

    public String getLineName() { return this.lineName; }
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDirection() { return this.direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getTypeOfTransportation() { return this.typeOfTransportation; }
    public void setTypeOfTransportation(String typeOfTransportation) { this.typeOfTransportation = typeOfTransportation; }

    public boolean isBarrierFree() { return this.barrierFree; }
    public void setBarrierFree(boolean barrierFree) { this.barrierFree = barrierFree; }

    public boolean isRealTimeSupported() { return this.realTimeSupported; }
    public void setRealTimeSupported(boolean realTimeSupported) { this.realTimeSupported = realTimeSupported; }

    public void setDepartureTime(List<String> departureTime) { this.departureTime = departureTime; }
    public List<String> getDepartureTime() { return this.departureTime; }

    /**
     * Maps this DTO to a {@link LineStation} object.
     *
     * @return new LineStation populated with data from this DTO
     */
    public LineStation mapToLine() {
        return new LineStation(
            this.getLineID(),
            this.getLineName(),
            this.getDirection(),
            this.getTypeOfTransportation(),
            this.isBarrierFree(),
            this.isRealTimeSupported(),
            this.getDepartureTime()
        );
    }

    /**
     * Root element of the API response.
     * <p>
     * Used by Jackson to map the top-level JSON object, containing the data field.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResponse {
        public Data data;
    }

    /**
     * Represents the data object in the API response.
     * <p>
     * Contains a list of monitors for each station.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public List<Monitors> monitors;
    }

    /**
     * Represents a station monitor in the API response.
     * <p>
     * Contains a list of lines monitored at the station.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Monitors {
        public List<Line> lines;
    }

    /**
     * Represents a transport line in the API response.
     * <p>
     * Contains line name, direction, type, barrier-free status, real-time support, line ID, and departures.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Line {
        public String name;
        public String towards   ;
        public String type;
        public boolean barrierFree;
        public boolean realtimeSupported;
        public String lineId;
        public Departures departures;
    }

    /**
     * Represents departures information in the API response.
     * <p>
     * Contains a list of departure entries for the line.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Departures {
        public List<Departure> departure;;
    }

    /**
     * Represents a single departure entry in the API response.
     * <p>
     * Contains departure time information.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Departure {
        public DepartureTime departureTime;
    }

    /**
     * Represents the departure time object in the API response.
     * <p>
     * Contains the planned departure time as a string.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DepartureTime  {
        public String timePlanned;
    }

}
