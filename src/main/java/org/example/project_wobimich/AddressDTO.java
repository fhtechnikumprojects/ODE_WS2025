package org.example.project_wobimich;

import java.util.List;

public class AddressDTO {
    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String city;
    private double longitude;
    private double latitude;


    public static class ApiResponse {
        public List<Feature> features;
    }

    public static class Feature {
        public Geometry geometry;
        public Properties properties;
    }

    public static class Geometry {
        public List<Double> coordinates;
    }

    public static class Properties {
        public String StreetName;
        public String StreetNumber;
        public String PostalCode;
        public String Municipality;
    }

}
