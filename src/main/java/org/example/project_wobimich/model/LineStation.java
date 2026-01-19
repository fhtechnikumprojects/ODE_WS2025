package org.example.project_wobimich.model;

import org.example.project_wobimich.utils.LineStationUtils;

import java.util.List;

/**
 * Represents a public transport line connected to a station.
 * <p>
 * Stores information such as line ID, name, direction, transportation type,
 * accessibility, real-time support and departure times.
 */
public class LineStation {
    private String id;
    private String name;
    private String direction;
    private String transportType;
    private boolean barrierFree;
    private boolean realTimeSupported;
    private List<String> departureTimes;

    /**
     * Creates a new LineStation with the given data.
     *
     * @param id unique identifier of the line
     * @param name name of the line (e.g., "U1", "D", "13A")
     * @param direction direction of travel
     * @param transportType type of transport (e.g., subway, tram, bus)
     * @param barrierFree whether the line supports barrier-free access
     * @param realTimeSupported whether real-time data is available
     * @param departureTimes list of departure times
     */
    public LineStation(String id, String name, String direction, String transportType, boolean barrierFree, boolean realTimeSupported, List<String> departureTimes) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.transportType = transportType;
        this.barrierFree = barrierFree;
        this.realTimeSupported = realTimeSupported;
        this.departureTimes = departureTimes;
    }

    /**
     * Default constructor.
     */
    public LineStation() {}

    /**
     * @return the name of the line
     * */
    public String getName() {
        return this.name;
    }

    /**
     * @return direction of the line
     * */
    public String getDirection() {
        return this.direction;
    }

    /**
     *  @return the transport type
     *  */
    public String getTypeOfTransportation() {
        return this.transportType;
    }

    /**
     * @return the list of departure times
     * */
    public List<String> getDepartureTimes() {
        return this.departureTimes;
    }

    /**
     * Returns a string representation of the line.
     *
     * @return formatted line information
     */
    @Override
    public String toString() {
        if (this.departureTimes != null) {
            String departureTime = LineStationUtils.getTimeInMin(this.getDepartureTimes().getFirst());
            return "Linie " + this.getName() + " | Richtung " + this.getDirection() + " | " + departureTime + " min";
        } else {
            return "Linie " + this.getName() + " | Richtung " + this.getDirection() + " | Keine Echtzeitinformationen vorhanden!";
        }
    }

}
