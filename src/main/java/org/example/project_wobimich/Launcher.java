package org.example.project_wobimich;

public class Launcher {
    public static void main(String[] args) {
        //Application.launch(WobimichApplication.class, args);

        /*
        Following Code is only for testing purpose:
         */
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

        /*
        RealTimeMonitorAPIClient departureTimeAPIClient = new RealTimeMonitorAPIClient("60200657");
        String response = departureTimeAPIClient.fetchAPIResponse();
        System.out.println(departureTimeAPIClient.parseAPIResponse(response).toPrettyString());
         */
    }
}