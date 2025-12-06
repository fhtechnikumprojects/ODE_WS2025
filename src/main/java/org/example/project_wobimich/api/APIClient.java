package org.example.project_wobimich.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class APIClient {
    protected abstract String getHost();
    protected abstract int getPort();
    protected abstract String getPath();
    protected abstract Socket createSocket(String host, int port) throws IOException;

    public String fetchAPIResponse() {
        StringBuilder response = new StringBuilder();

        try (Socket socket = createSocket(getHost(), getPort())) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET " + getPath() + " HTTP/1.1");
            out.println("Host: " + getHost());
            out.println("Connection: close");
            out.println();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            //Skip header
            while ((line = in.readLine()) != null && !line.isEmpty()) {}

            //read body of json
            while((line = in.readLine()) != null) {
                //skip chunk lines of HTTPS
                if (line.matches("^[0-9a-fA-F]+$")) {
                    continue;
                }
                response.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }


}
