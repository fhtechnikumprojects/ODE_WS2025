package org.example.project_wobimich.model;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a provided address/location.
 * <p>
 * Stores information such as street name, street number, postal code, city,
 * and geographic coordinates (latitude and longitude).
 */
public class Location {
    private String streetName;
    private String streetNumber;
    private double longitude;
    private double latitude;

    /**
     * Constructs a Location with all fields initialized.
     *
     * @param streetName street name of the location
     * @param streetNumber street number of the location
     * @param longitude longitude coordinate
     * @param latitude latitude coordinate
     */
    public Location(String streetName, String streetNumber, double longitude, double latitude) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return the street name of the location
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * @param streetName the street name to set
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * @return the street number of the location
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * @param streetNumber the street number to set
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * @return the longitude coordinate
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude coordinate to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude coordinate
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude coordinate to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


}
