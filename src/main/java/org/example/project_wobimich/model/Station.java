package org.example.project_wobimich.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a public transport station in Vienna.
 * <p>
 * Stores basic information such as station ID, name, and geographic
 * coordinates. It also contains all transport lines that are connected
 * to this station.
 */
public class Station {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<LineStation> lines;

    /**
     * Creates a new station with the given data.
     *
     * @param id unique station ID
     * @param name station name
     * @param latitude latitude coordinate
     * @param longitude longitude coordinate
     */
    public Station(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lines = new ArrayList<>();
    }

    /**
     * @return the station ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id new station ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the station name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name new station name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return latitude value
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * @param latitude new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return longitude value
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * @param longitude new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return list of connected transport lines
     */
    public List<LineStation> getLines() {
        return this.lines;
    }

    /**
     * @param lines transport lines belonging to this station
     */
    public void setLines(List<LineStation> lines) {
        this.lines = lines;
    }
}
