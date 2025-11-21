package org.example.project_wobimich;

public class Address {
    private String street;
    private String number;
    private double latitude;
    private double longitude;

    public Address(String street, String number, double latitude, double longitude) {
        this.street = street;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}
