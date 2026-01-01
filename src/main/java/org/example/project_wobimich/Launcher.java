package org.example.project_wobimich;

import org.example.project_wobimich.api.AddressAPIClient;
import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.AddressDTO;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.LineStation;
import org.example.project_wobimich.model.Location;
import org.example.project_wobimich.model.Station;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        /*

        //Location of User-Input
        System.out.println("Location of User-Input: ");
        String address = "Wiesengasse";
        AddressAPIClient addressAPIClient = new AddressAPIClient(address);
        String apiResponse = addressAPIClient.fetchAPIResponse();
        AddressDTO addressDTO = addressAPIClient.parseAPIResponse(apiResponse);
        Location location = addressDTO.mapToUserLocation();

        System.out.println("Strassenname: " + location.getStreetName());
        System.out.println("Strassennummer: " + location.getStreetNumber());
        System.out.println("Longitude: " + location.getLongitude());
        System.out.println("Latitude: " + location.getLatitude());
        System.out.println("\n");

        //Show 5 closest station to address of given location
        ArrayList<Station> stations = StationUtils.listStationsByDistanceFrom(location);
        StationUtils.sortAscending(stations, Comparator.comparing(Station::getDistance));

        for (Station station : stations) {
            System.out.println("station: " + station.getName());
            System.out.println("distance to location: " + station.getDistance());
            System.out.println("--------------------------------------------------");
        }


*/

        /*
        System.out.println("Show all line information of a station:");

        for (int i = 0; i < 5; i++) {
            System.out.println("Station name: " + stations.get(i).getName());
            RealTimeMonitorAPIClient realTimeMonitorAPIClient = new RealTimeMonitorAPIClient(stations.get(i).getId());
            String responseRealTimeMonitorAPI = realTimeMonitorAPIClient.fetchAPIResponse();
            List<RealTimeMonitorDTO> listRealTimeMonitor = realTimeMonitorAPIClient.parseAPIResponse(responseRealTimeMonitorAPI);
            List<LineStation> lines = new ArrayList<LineStation>();

            for(RealTimeMonitorDTO RTM : listRealTimeMonitor){
                lines.add(RTM.mapToLine());
            }

            //print information of a line
            for(LineStation line : lines) {
                System.out.println("lineID: " + line.getId());
                System.out.println("lineName: " + line.getName());
                System.out.println("towards: " + line.getDirection());
                System.out.println("typeOfTransportation: " + line.getTypeOfTransportation());
                System.out.println("barrierFree: " + line.isBarrierFree());
                System.out.println("realTimeSupport: " + line.isRealTimeSupported());
                String departureTimeOutput = "[";

                for(String depTime : line.getDepartureTime()) {
                    departureTimeOutput += depTime + ",";
                }
                departureTimeOutput += "]";
                System.out.println(departureTimeOutput);
                System.out.println("\n");
            }
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("\n");
        }

         */



    }
}