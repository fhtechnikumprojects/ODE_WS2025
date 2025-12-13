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
     * Default constructor.
     */
    public Location() {}

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

    /**
     * Calculates the distance between two locations in kilometer
     * using the Haversine formula.
     *
     * The Haversine formula:
     *   a = sin²(Δφ/2) + cos(φ1) * cos(φ2) * sin²(Δλ/2)
     *   c = 2 * atan2(√a, √(1−a))
     *   d = R * c
     *
     * The result is always positive since d = R * c >= 0.
     * The result is exactly 0 if the coordinates of both locations loc1 and loc2 are equal.
     * (Fault tolerance about ~ 60 meters)
     *
     * @param loc1 The first location
     * @param loc2 The second location
     * @return The distance between loc1 and loc2 in kilometers
     */
    public double distanceBetween(Location loc1, Location loc2) {
        double earthRadiusKM = 6371;
        double phi1 = Math.toRadians(loc1.getLatitude());
        double phi2 = Math.toRadians(loc2.getLatitude());
        double deltaPhi = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double deltaLambda = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadiusKM * c;

        return distance;
    }

    /**
     * Returns a list of all public transportation stations in Vienna with their ID
     * and the distance from this location.
     *
     * @return list of stations with distance
     */
    public ArrayList<Station> listStationsByDistanceFrom() {
        ArrayList<Station> stations = new ArrayList<>();
        File pathToJsonFile = new File("src/main/resources/org/example/project_wobimich/data/wl-ogd-haltestellen.json");

        try {
            ObjectMapper mapper = new ObjectMapper();

            List<Station> stationJson= mapper.readValue(
                    pathToJsonFile,
                    new TypeReference<List<Station>>(){}
            );

            for (Station stJson : stationJson) {
                Station currentStation = new Station(
                        stJson.getId(),
                        stJson.getName(),
                        stJson.getLatitude(),
                        stJson.getLongitude()
                );

                Location jsonStationLocation = new Location();
                jsonStationLocation.setStreetName(stJson.getName());
                jsonStationLocation.setLongitude(stJson.getLongitude());
                jsonStationLocation.setLatitude(stJson.getLatitude());

                double distance = this.distanceBetween(this,jsonStationLocation);
                currentStation.setDistance(distance);

                stations.add(currentStation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stations;
    }

    /**
     * Sort given list of any type in ascending order.
     */
    public <T> void sortAscending(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
    }


}
