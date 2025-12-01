package org.example.project_wobimich;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class AddressDTO {
    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String city;
    private double longitude;
    private double latitude;

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    /*
    Subclasses
    */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResponse {
        public List<Feature> features;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        public Geometry geometry;
        public Properties properties;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        public List<Double> coordinates;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {
        public String StreetName;
        public String StreetNumber;
        public String PostalCode;
        public String Municipality;
    }

}
