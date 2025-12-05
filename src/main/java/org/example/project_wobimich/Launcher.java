package org.example.project_wobimich;

import org.example.project_wobimich.api.RealTimeMonitorAPIClient;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;
import org.example.project_wobimich.model.Station;

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