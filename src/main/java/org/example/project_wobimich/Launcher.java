package org.example.project_wobimich;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        UserAddress userAddress = new UserAddress("Jägerstraße", "10");
        AdressAPIClient address = new AdressAPIClient(userAddress.getStreetName(), userAddress.getStreetNumber());
        System.out.println(address.fetchAPIResponse());
        System.out.println(address.parseAPIResponse(address.fetchAPIResponse()).toPrettyString());

        /*
        RealTimeMonitorAPIClient departureTimeAPIClient = new RealTimeMonitorAPIClient("60200657");
        String response = departureTimeAPIClient.fetchAPIResponse();
        System.out.println(departureTimeAPIClient.parseAPIResponse(response).toPrettyString());
         */
    }
}