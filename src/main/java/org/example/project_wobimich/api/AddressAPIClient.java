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

    /**
     * Returns the host name of the API server.
     *
     * @return the API host
     */
    @Override
    protected String getHost() {
        return HOST;
    }

    /**
     * Returns the port number used for the API connection.
     *
     * @return the API port
     */
    @Override
    protected int getPort() {
        return PORT;
    }

    /**
     * Returns the HTTP request path including query parameters.
     *
     * @return the API request path
     */
    @Override
    protected String getPath() {
        return this.path;
    }

    /**
     * Creates a plain TCP socket for HTTP communication.
     *
     * @param host the remote host to connect to
     * @param port the remote port to connect to
     * @return an open {@link Socket} connected to the given host and port
     * @throws IOException if the socket cannot be created
     */
    @Override
    protected Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    /**
     * Parses the JSON response into an {@link AddressDTO}.
     * <p>
     * Extracts street name, street number and geographic coordinates
     * (longitude and latitude) from the API response.
     *
     * @param response raw JSON response returned by the API
     * @return an {@link AddressDTO} containing the parsed address data
     * @throws ApiException if parsing the JSON response fails
     */
    public AddressDTO parseAPIResponse(String response) throws ApiException {
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
        } catch (IOException e) {
            throw new ApiException("Parsing of API data failed!", e);
        }
    }

}