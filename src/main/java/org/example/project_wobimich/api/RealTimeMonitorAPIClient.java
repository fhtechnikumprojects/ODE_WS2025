package org.example.project_wobimich.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * API client for requesting real-time monitor data from Wiener Linien.
 * <p>
 * Establishes an HTTPS connection and extracts transport line information,
 * including upcoming departure times.
 */
public class RealTimeMonitorAPIClient extends APIClient {
    private static final String HOST = "www.wienerlinien.at";
    private static final int PORT = 443;
    private final String path;

    /**
     * Creates a client instance for a specific station using its DIVA ID.
     *
     * @param divaID station identifier used by Wiener Linien
     */
    public RealTimeMonitorAPIClient(String divaID) {
        this.path = "/ogd_realtime/monitor?diva=" + divaID;
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
     * Creates an SSL-enabled socket connection and performs the TLS handshake.
     */
    @Override
    protected Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.startHandshake();
        return socket;
    }

    /**
     * Parses the JSON response and returns a list of real-time monitor entries.
     * Extracts only relevant fields such as line name, direction, and
     * planned departure times.
     *
     * @param response raw JSON response
     * @return a list of real-time monitor DTOs
     */
    public List<RealTimeMonitorDTO> parseAPIResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        List<RealTimeMonitorDTO> listOfLines = new ArrayList<>();

        try {
            RealTimeMonitorDTO.ApiResponse api = mapper.readValue(response, RealTimeMonitorDTO.ApiResponse.class);
            for (RealTimeMonitorDTO.Monitors monitor : api.data.monitors) {
                if (monitor != null && monitor.lines != null) {
                    for (RealTimeMonitorDTO.Line line : monitor.lines) {
                        if (line != null) {
                            RealTimeMonitorDTO lineDTO = new RealTimeMonitorDTO();
                            lineDTO.setLineID(line.lineId);
                            lineDTO.setDirection(line.towards);
                            lineDTO.setLineName(line.name);
                            lineDTO.setTypeOfTransportation(line.type);
                            lineDTO.setBarrierFree(line.barrierFree);
                            lineDTO.setRealTimeSupported(line.realtimeSupported);

                            List<String> departureTimes = new ArrayList<>();
                            if (line.departures != null && line.departures.departure != null) {
                                for (RealTimeMonitorDTO.Departure dep : line.departures.departure) {
                                    departureTimes.add(dep.departureTime.timePlanned);
                                }
                            }
                            lineDTO.setDepartureTime(departureTimes);
                            listOfLines.add(lineDTO);
                        }
                    }
                }
            }
        }
         catch(IOException e) {
            e.printStackTrace();
        }
        return listOfLines;
    }


}

