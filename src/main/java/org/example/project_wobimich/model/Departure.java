package org.example.project_wobimich.model;

public class Departure {
    private String lineID;
    private String lineName;
    private String lineDirection;
    private boolean realTime;
    private int departureTime; //in minutes

    public Departure(String lineID, String lineName, String lineDirection, boolean realTime, int departureTime) {
        this.lineID = lineID;
        this.lineName = lineName;
        this.lineDirection = lineDirection;
        this.realTime = realTime;
        this.departureTime = departureTime;
    }


}
