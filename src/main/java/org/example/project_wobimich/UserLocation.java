package org.example.project_wobimich;


/**
 * Represents a user-provided address/location.
 * <p>
 * Stores information such as street name, street number, postal code, city,
 * and geographic coordinates (latitude and longitude).
 */
public class UserLocation {
    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String city;
    private double longitude;
    private double latitude;

    /**
     * Constructs a UserLocation with all fields initialized.
     *
     * @param streetName street name of the location
     * @param streetNumber street number of the location
     * @param postalCode postal code
     * @param city city name
     * @param longitude longitude coordinate
     * @param latitude latitude coordinate
     */
    public UserLocation(String streetName, String streetNumber, String postalCode, String city, double longitude, double latitude) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    /**
     * Default constructor.
     */
    public UserLocation() {}

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
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     *  @param city the city name to set
     */
    public void setCity(String city) {
        this.city = city;
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
