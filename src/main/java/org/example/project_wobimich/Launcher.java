package org.example.project_wobimich;

import org.example.project_wobimich.api.AddressAPIClient;
import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.AddressDTO;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.Station;

import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        String streetName = "Porzellangasse";
        String streetNumber = "2";
        AddressAPIClient addressAPIClient = new AddressAPIClient(streetName,streetNumber);
        String apiResponse = addressAPIClient.fetchAPIResponse();
        System.out.println(apiResponse);
        AddressDTO addressDTO = addressAPIClient.parseAPIResponse(apiResponse);
        System.out.println("Strassenname: " + addressDTO.getStreetName());
        System.out.println("Strassennummer: " + addressDTO.getStreetNumber());
        System.out.println("Postleitzahl: " + addressDTO.getPostalCode());
        System.out.println("Stadt: " + addressDTO.getCity());
        System.out.println("Longitude: " + addressDTO.getLongitude());
        System.out.println("Latitude: " + addressDTO.getLatitude());

        System.out.println("Show all line information of a station:");
        Station station = new Station("60200657","Karlsplatz",16.3689484,48.2009554);
        RealTimeMonitorAPIClient realTimeMonitorAPIClient = new RealTimeMonitorAPIClient("60200657");
        String responseRealTimeMonitorAPI = realTimeMonitorAPIClient.fetchAPIResponse();
        List<RealTimeMonitorDTO> listRealTimeMonitor = realTimeMonitorAPIClient.parseAPIResponse(responseRealTimeMonitorAPI);

        for(RealTimeMonitorDTO RTM : listRealTimeMonitor) {
            System.out.println("lineID: " + RTM.getLineID());
            System.out.println("lineName: " + RTM.getLineName());
            System.out.println("towards: " + RTM.getDirection());
            System.out.println("typeOfTransportation: " + RTM.getTypeOfTransportation());
            System.out.println("barrierFree: " + RTM.isBarrierFree());
            System.out.println("realTimeSupport: " + RTM.isRealTimeSupported());
            String departureTimeOutput = "[";

            for(String depTime : RTM.getDepartureTime()) {
                departureTimeOutput += depTime + ",";
            }
            departureTimeOutput += "]";
            System.out.println(departureTimeOutput);
            System.out.println("\n");
        }



    }
}