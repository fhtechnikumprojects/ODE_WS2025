package org.example.project_wobimich;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class RealTimeMonitorDTO {
    private String lineID;
    private String lineName;
    private String direction;
    private String typeOfTransportation;
    private boolean barrierFree;
    private boolean realTimeSupported;

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
        public LocationStop locationStop;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationStop {
        public List<Line> lines;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Line {
        public String name;
        public String direction;
        public String type;
        public boolean barrierFree;
        public boolean realTimeSupported;
        public int lineId;
        public List<Departures> departures;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Departures {
        public String timeReal;
        public String timePlanned;
    }










}
