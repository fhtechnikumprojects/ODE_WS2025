package org.example.project_wobimich.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.dto.AddressDTO;

import java.io.IOException;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * API client used to query address information from data.wien.gv.at.
 * <p>
 * Fetches detailed geographic information for a specific address, such as
 * coordinates, street name, postal code, and municipality.
 */
public class AddressAPIClient extends APIClient {
    private static final String HOST = "www.data.wien.gv.at";
    private static final int PORT = 80;
    private final String path;

    /**
     * Creates a client instance for a specific address.
     *
     * @param userInput
     */
    public AddressAPIClient(String userInput) {
        String address = URLEncoder.encode(userInput, StandardCharsets.UTF_8);
        this.path = "/daten/OGDAddressService.svc/GetAddressInfo?Address=" + address + "&crs=EPSG:4326";
    }

    @Override
    protected String getHost() {
        return HOST;
    }

    @Override
    protected int getPort() {
        return PORT;
    }

    @Override
    protected String getPath() {
        return this.path;
    }

    /**
     * Creates a plain TCP socket for HTTP communication.
     */
    //    service has to catch the exception!!! ==> need to be considered when implementing the service (-class)
    @Override
    protected Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    /**
     * Parses the JSON response into an {@link AddressDTO} containing
     * address-related information.
     *
     * @param response raw JSON response
     * @return an AddressDTO with extracted address details, or null on error
     */
    public AddressDTO parseAPIResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            AddressDTO.ApiResponse api = mapper.readValue(response, AddressDTO.ApiResponse.class);
            AddressDTO.Feature feature = api.features.getFirst();

            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreetName(feature.properties.StreetName);
            addressDTO.setStreetNumber(feature.properties.StreetNumber);
            addressDTO.setLongitude(feature.geometry.coordinates.get(0));
            addressDTO.setLatitude(feature.geometry.coordinates.get(1));

            return addressDTO;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}