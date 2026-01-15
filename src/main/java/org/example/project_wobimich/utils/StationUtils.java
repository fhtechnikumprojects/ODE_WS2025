package org.example.project_wobimich.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.model.Location;
import org.example.project_wobimich.model.Station;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StationUtils {

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
    public static double distanceBetween(Location loc1, Location loc2) {
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
     * and the distance from given location.
     *
     * @return list of stations with distance
     */
    public static ArrayList<Station> listStationsByDistanceFrom(Location location) {
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

                Location jsonStationLocation = new Location(
                        stJson.getName(),
                        "0", //placeholder
                        stJson.getLongitude(),
                        stJson.getLatitude()
                );

                double distance = StationUtils.distanceBetween(location,jsonStationLocation);
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
    public static <T> void sortAscending(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
    }

    /**
     *
     * @return a list of 5 closet station to given station
     */
    public static ArrayList<Station> closestStationToLocation(ArrayList<Station> stations) {
        ArrayList<Station> closestStations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            closestStations.add(stations.get(i));
        }
        return closestStations;
    }
}
