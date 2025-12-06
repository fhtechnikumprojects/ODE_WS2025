package org.example.project_wobimich.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class RealTimeMonitorAPIClient extends APIClient {
    private static final String HOST = "www.wienerlinien.at";
    private static final int PORT = 443;
    private final String path;

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

    @Override
    protected Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.startHandshake();
        return socket;
    }

    //response of API request is transformed to JSON-format
    //set parse given String-response and retrieve only specific fields of JSON-response
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

