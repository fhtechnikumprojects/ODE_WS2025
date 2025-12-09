package org.example.project_wobimich.model;

import java.util.List;

public class Line {
    private String id;
    private String name;
    private String direction;
    private String typeOfTransportation;
    private boolean barrierFree;
    private boolean realTimeSupported;
    private List<String> departureTime;

    /*
    constructor with arguments: initialize fields
     */
    public Line (String id, String name, String direction, String typeOfTransportation, boolean barrierFree, boolean realTimeSupported, List<String> departureTime) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.typeOfTransportation = typeOfTransportation;
        this.barrierFree = barrierFree;
        this.realTimeSupported = realTimeSupported;
        this.departureTime = departureTime;
    }

    /*
    constructor without arguments
     */
    public Line() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isBarrierFree() {
        return barrierFree;
    }

    public void setBarrierFree(boolean barrierFree) {
        this.barrierFree = barrierFree;
    }

    public boolean isRealTimeSupported() {
        return realTimeSupported;
    }

    public void setRealTimeSupported(boolean realTimeSupported) {
        this.realTimeSupported = realTimeSupported;
    }

    public String getTypeOfTransportation() {
        return typeOfTransportation;
    }

    public void setTypeOfTransportation(String typeOfTransportation) {
        this.typeOfTransportation = typeOfTransportation;
    }

    public List<String> getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(List<String> departureTime) {
        this.departureTime = departureTime;
    }

}
