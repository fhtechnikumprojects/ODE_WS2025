package org.example.project_wobimich.model;

public class Line {
    private String id;
    private String name;
    private String direction;
    private boolean barrierFree;
    private boolean realTimeSupported;
    private String typeOfTransportation;
    private String departure;

    public Line (String id, String name, String direction, boolean barrierFree, boolean realTimeSupported, String typeOfTransportation) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.barrierFree = barrierFree;
        this.realTimeSupported = realTimeSupported;
        this.typeOfTransportation = typeOfTransportation;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

}
