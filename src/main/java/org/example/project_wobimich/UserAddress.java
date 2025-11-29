package org.example.project_wobimich;


/*
This class is used to store user input as address (street name and street number).
 */
public class UserAddress {
    private String streetName;
    private String streetNumber;
    private String zipCode;
    private double longitude;
    private double latitude;

    public UserAddress(String streetName, String streetNumber) {
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
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

}
