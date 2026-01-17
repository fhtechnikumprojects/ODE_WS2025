package org.example.project_wobimich.model;

import org.example.project_wobimich.utils.LineStationUtils;

import java.util.List;

/**
 * Represents a transport line connected to a station.
 * <p>
 * Stores information such as the line ID, name, direction, transportation type,
 * barrier-free access, real-time support, and scheduled or real-time departure times.
 */
public class LineStation {
    private String id;
    private String name;
    private String direction;
    private String typeOfTransportation;
    private boolean barrierFree;
    private boolean realTimeSupported;
    private List<String> departureTime;

    /**
     * Creates a new LineStation with the given data.
     *
     * @param id unique identifier of the line
     * @param name name of the line (e.g., "U1", "D", "13A")
     * @param direction direction of travel
     * @param typeOfTransportation type of transport (e.g., subway, tram, bus)
     * @param barrierFree whether the line supports barrier-free access
     * @param realTimeSupported whether real-time data is available
     * @param departureTime list of departure times
     */
    public LineStation(String id, String name, String direction, String typeOfTransportation, boolean barrierFree, boolean realTimeSupported, List<String> departureTime) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.typeOfTransportation = typeOfTransportation;
        this.barrierFree = barrierFree;
        this.realTimeSupported = realTimeSupported;
        this.departureTime = departureTime;
    }

    /**
     * Default constructor.
     */
    public LineStation() {}

    /**
     * @return the line ID
     * */
    public String getId() {
        return id;
    }

    /**
     * @param id new line ID
     * */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name of the line
     * */
    public String getName() {
        return name;
    }

    /**
     * @param name new line name
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return direction of the line
     * */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction new direction
     * */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return true if the line supports barrier-free access
     * */
    public boolean isBarrierFree() {
        return barrierFree;
    }

    /**
     * @param barrierFree barrier-free support flag
     * */
    public void setBarrierFree(boolean barrierFree) {
        this.barrierFree = barrierFree;
    }

    /**
     * @return true if real-time data is available
     * */
    public boolean isRealTimeSupported() {
        return realTimeSupported;
    }

    /**
     * @param realTimeSupported real-time support flag
     * */
    public void setRealTimeSupported(boolean realTimeSupported) {
        this.realTimeSupported = realTimeSupported;
    }

    /**
     *  @return the transport type
     *  */
    public String getTypeOfTransportation() {
        return typeOfTransportation;
    }

    /**
     * @param typeOfTransportation new transport type
     * */
    public void setTypeOfTransportation(String typeOfTransportation) {
        this.typeOfTransportation = typeOfTransportation;
    }

    /**
     * @return the list of departure times
     * */
    public List<String> getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime new list of departure times
     * */
    public void setDepartureTime(List<String> departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        String departureTime = LineStationUtils.getTimeInMin(this.getDepartureTime().getFirst());
        return "Linie " + this.getName() + " | Richtung " + this.getDirection() + " | " + departureTime + " min";
    }

}
