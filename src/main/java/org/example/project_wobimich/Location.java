package org.example.project_wobimich;

public class Location {
    private String streetName;
    private String streetNumber;
    private double longitude;
    private double latitude;

    public Location(String streetName, String streetNumber) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
