package org.example.project_wobimich.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.project_wobimich.model.LineStation;

import java.util.List;

public class RealTimeMonitorDTO {
    private String lineID;
    private String lineName;
    private String direction;
    private String typeOfTransportation;
    private boolean barrierFree;
    private boolean realTimeSupported;
    private List<String> departureTime;

    public String getLineID() {
        return this.lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public String getLineName() {
        return this.lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTypeOfTransportation() {
        return this.typeOfTransportation;
    }

    public void setTypeOfTransportation(String typeOfTransportation) {
        this.typeOfTransportation = typeOfTransportation;
    }

    public boolean isBarrierFree() {
        return this.barrierFree;
    }

    public void setBarrierFree(boolean barrierFree) {
        this.barrierFree = barrierFree;
    }

    public boolean isRealTimeSupported() {
        return this.realTimeSupported;
    }

    public void setRealTimeSupported(boolean realTimeSupported) {
        this.realTimeSupported = realTimeSupported;
    }

    public void setDepartureTime(List<String> departureTime) {
        this.departureTime = departureTime;
    }

    public List<String> getDepartureTime() {
        return this.departureTime;
    }

    public LineStation mapToLine(LineStation line) {
        line = new LineStation(
                this.getLineID(),
                this.getLineName(),
                this.getDirection(),
                this.getTypeOfTransportation(),
                this.isBarrierFree(),
                this.isRealTimeSupported(),
                this.getDepartureTime()
                );
        return line;
    }


    /*
    subclasses
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResponse {
        public Data data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public List<Monitors> monitors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Monitors {
        public List<Line> lines;
    }

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Departures {
        public List<Departure> departure;;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Departure {
        public DepartureTime departureTime;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DepartureTime  {
        public String timePlanned;
    }

}
