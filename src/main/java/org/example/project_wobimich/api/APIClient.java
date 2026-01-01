package org.example.project_wobimich.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Abstract base class for API clients communicating with external web services.
 * <p>
 * Provides shared logic for sending HTTP requests over a socket connection and
 * retrieving the response body. Subclasses must define host, port, path, and
 * how the socket is created (e.g., plain TCP or SSL).
 */
public abstract class APIClient {
    /**
     * @return the host address of the API
     * */
    protected abstract String getHost();

    /**
     * @return the port used for the API request
     * */
    protected abstract int getPort();

    /**
     * @return the request path that will be appended to the host
     * */
    protected abstract String getPath();

    /**
     * Creates a socket connection to the given host and port.
     * Subclasses define whether the connection is plain or SSL.
     */
    protected abstract Socket createSocket(String host, int port) throws IOException;

    /**
     * Sends a GET request to the configured API endpoint and returns the
     * response body as a string.
     *
     * @return the response body from the API
     */
    public String fetchAPIResponse() throws IOException {
        StringBuilder response = new StringBuilder();

        try (Socket socket = createSocket(getHost(), getPort())) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET " + getPath() + " HTTP/1.1");
            out.println("Host: " + getHost());
            out.println("Connection: close");
            out.println();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            //Skip HTTP header lines
            while ((line = in.readLine()) != null && !line.isEmpty()) {}

            //Read body of JSON
            while((line = in.readLine()) != null) {
                if (line.matches("^[0-9a-fA-F]+$")) {
                    continue;
                }
                response.append(line);
            }
        }
        return response.toString();
    }


}
