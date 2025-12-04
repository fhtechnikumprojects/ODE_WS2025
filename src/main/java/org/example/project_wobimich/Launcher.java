package org.example.project_wobimich;

import org.example.project_wobimich.model.Station;

import java.util.ArrayList;
import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        /*
        Following Code is only for testing purpose:
         */
        /*
        UserLocationAddress userAddress = new UserLocationAddress("Klosterneuburger Straße", "2");
        AddressAPIClient address = new AddressAPIClient(userAddress.getStreetName(), userAddress.getStreetNumber());
        String responseAPI = address.fetchAPIResponse();
        System.out.println("Raw API response:" + responseAPI + "\n");

        System.out.println("\n JSON API response stored in an UserLocationAddress-Object\n");
        AddressDTO addressDTO = address.parseAPIResponse(responseAPI);
        System.out.println("Strassenname: " + addressDTO.getStreetName());
        System.out.println("Strassennummer: " + addressDTO.getStreetNumber());
        System.out.println("Stadt: " + addressDTO.getCity());
        System.out.println("Postleitzahl: " + addressDTO.getPostalCode());
        System.out.println("Longitude: " + addressDTO.getLongitude());
        System.out.println("Latitude: " + addressDTO.getLatitude());
         */

        /*
        System.out.println("Web-API Real time monitor:");
        Station station = new Station("60200657","Karlsplatz",16.3689484,48.2009554);
        RealTimeMonitorAPIClient realTimeMonitorAPIClient = new RealTimeMonitorAPIClient("60200657");
        String responseRealTimeMonitorAPI = realTimeMonitorAPIClient.fetchAPIResponse();

        List<RealTimeMonitorDTO> listRealTimeMonitor = realTimeMonitorAPIClient.parseAPIResponse(responseRealTimeMonitorAPI);
        List<String> departureTime = listRealTimeMonitor.getFirst().getDepartureTime();

        System.out.println("lineID: " + listRealTimeMonitor.getFirst().getLineID());
        System.out.println("lineName: " + listRealTimeMonitor.getFirst().getLineName());
        System.out.println("towards: " + listRealTimeMonitor.getFirst().getTowards());
        System.out.println("typeOfTransportation: " + listRealTimeMonitor.getFirst().getTypeOfTransportation());
        System.out.println("barrierFree: " + listRealTimeMonitor.getFirst().isBarrierFree());
        System.out.println("realTimeSupport: " + listRealTimeMonitor.getFirst().isRealTimeSupported());
        String departureTimeOutput = "[";

        for(String depTime : departureTime) {
            departureTimeOutput += depTime + ",";
        }
        departureTimeOutput += "]";
        System.out.println(departureTimeOutput);
        */

    }
}