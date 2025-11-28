package org.example.project_wobimich.model;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private String id;
    private String name;
    private String type;
    private String direction;
    private List<Departure> departures;

    public Line(String id, String name, String type, String direction) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.direction = direction;
        this.departures = new ArrayList<>();
    }

}
