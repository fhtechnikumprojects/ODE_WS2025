package org.example.project_wobimich.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.project_wobimich.model.Location;

import java.util.List;

/**
 * Data Transfer Object for address information from the Vienna API.
 * <p>
 * Contains street name, street number, postal code, city, and geographic coordinates.
 * Can map its data to a {@link Location}.
 */
public class AddressDTO {
    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String city;
    private double longitude;
    private double latitude;

    public String getStreetName() { return this.streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public String getStreetNumber() { return this.streetNumber; }
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }

    public double getLongitude() { return this.longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return this.latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    /**
     * Maps the DTO fields to a {@link Location} domain object.
     *
     * @return a {@link Location} created from the DTO data
     */
    public Location mapToUserLocation() {
        return new Location(this.getStreetName(),this.getStreetNumber(),this.getLongitude(),this.getLatitude());
    }

    /**
     * Root element of the API response.
     * <p>
     * Used by Jackson to map the top-level JSON object containing
     * a list of address features.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResponse {
        public List<Feature> features;
    }

    /**
     * Represents a single feature in the API response.
     * <p>
     * Contains geometry and property information for an address.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        public Geometry geometry;
        public Properties properties;
    }

    /**
     * Represents the geometry object in the API response.
     * <p>
     * Coordinates are stored as [longitude, latitude].
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        public List<Double> coordinates;
    }

    /**
     * Represents the properties object in the API response.
     * <p>
     * Contains address metadata such as street, postal code and municipality.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {
        public String StreetName;
        public String StreetNumber;
        public String PostalCode;
        public String Municipality;
    }
}
