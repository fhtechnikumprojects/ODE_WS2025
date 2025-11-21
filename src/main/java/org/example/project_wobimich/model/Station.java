package org.example.project_wobimich.model;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Line> lines;

    public Station(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lines = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public List<Line> getLines() {
        return this.lines;
    }

}
