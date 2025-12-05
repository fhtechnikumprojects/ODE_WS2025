package org.example.project_wobimich.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_wobimich.dto.RealTimeMonitorDTO;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class RealTimeMonitorAPIClient {
    private static final String HOST = "www.wienerlinien.at";
    private static final int PORT = 443;
    private String path;
    private String divaID; //for station that has lines assigned to it
    private String stopID; //for station point where lines are departure

    public RealTimeMonitorAPIClient(String divaID) {
        this.divaID = divaID;
        this.path = "/ogd_realtime/monitor?diva=" + divaID;
    }

    /*
    Send a request to the API.
    Get request and return it as a string
    The Wiener Linien OGD Realtime API can only be accessed using HTTPS.
    A normal socket uses HTTP on port 80, so it receives a 302 redirect.
    A raw socket cannot perform the SSL/TLS handshake, so it cannot follow the redirect to HTTPS.
    With SSLSocketFactory, I create an SSLSocket that performs the TLS handshake and establishes a proper HTTPS connection on port 443
     */
    public String fetchAPIResponse() {
        StringBuilder response = new StringBuilder();
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try (SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT)) {
            socket.startHandshake();

            //send request
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET " + this.path + " HTTP/1.1");
            out.println("Host: " + HOST);
            out.println("Connection: close");
            out.println();
            out.flush();

            //read response
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            boolean isBody = false;

            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    isBody = true;
                    continue;
                }
                if (isBody) {
                    // Skip chunk size lines  ==> start at body
                    if (line.matches("^[0-9a-fA-F]+$")) {
                        continue;
                    }
                    response.append(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
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

