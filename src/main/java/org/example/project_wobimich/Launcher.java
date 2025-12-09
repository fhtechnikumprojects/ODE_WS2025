package org.example.project_wobimich;

import org.example.project_wobimich.api.AddressAPIClient;
import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.AddressDTO;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.mapper.LineMapper;
import org.example.project_wobimich.mapper.UserLocationMapper;
import org.example.project_wobimich.model.Line;
import org.example.project_wobimich.model.Station;

import java.util.ArrayList;
import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        String streetName = "Höchststädtplatz";
        String streetNumber = "2";
        AddressAPIClient addressAPIClient = new AddressAPIClient(streetName,streetNumber);
        String apiResponse = addressAPIClient.fetchAPIResponse();
        AddressDTO addressDTO = addressAPIClient.parseAPIResponse(apiResponse);
        UserLocation location = UserLocationMapper.mapToUserLocation(addressDTO);

        System.out.println("Strassenname: " + location.getStreetName());
        System.out.println("Strassennummer: " + location.getStreetNumber());
        System.out.println("Postleitzahl: " + location.getPostalCode());
        System.out.println("Stadt: " + location.getCity());
        System.out.println("Longitude: " + location.getLongitude());
        System.out.println("Latitude: " + location.getLatitude());

        System.out.println("Show all line information of a station:");
        Station station = new Station("60200657","Karlsplatz",16.3689484,48.2009554);
        RealTimeMonitorAPIClient realTimeMonitorAPIClient = new RealTimeMonitorAPIClient("60200657");
        String responseRealTimeMonitorAPI = realTimeMonitorAPIClient.fetchAPIResponse();
        List<RealTimeMonitorDTO> listRealTimeMonitor = realTimeMonitorAPIClient.parseAPIResponse(responseRealTimeMonitorAPI);
        List<Line> lines = new ArrayList<Line>();

        for(RealTimeMonitorDTO RTM : listRealTimeMonitor){
            lines.add((LineMapper.mapToLine(RTM)));
        }

        for(Line line : lines) {
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



    }
}