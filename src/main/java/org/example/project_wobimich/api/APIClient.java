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
     * Returns the host address of the API.
     *
     * @return the API host
     */
    protected abstract String getHost();

    /**
     * Returns the port used for the API request.
     *
     * @return the API port
     */
    protected abstract int getPort();

    /**
     * Returns the request path that will be appended to the host.
     *
     * @return the API request path
     */
    protected abstract String getPath();

    /**
     * Creates a socket connection to the given host and port.
     * <p>
     * Subclasses define whether the connection is a plain TCP socket
     * or a secure SSL socket.
     *
     * @param host the remote host to connect to
     * @param port the remote port to connect to
     * @return an open {@link Socket} connected to the given host and port
     * @throws IOException if the socket cannot be created
     */
    protected abstract Socket createSocket(String host, int port) throws IOException;

    /**
     * Sends an HTTP GET request to the configured API endpoint and returns
     * the response body as a string.
     * <p>
     * The HTTP header is skipped and only the response body (JSON) is returned.
     * Chunk size lines (hexadecimal values) are ignored/skipped.
     *
     * @return the response body returned by the API
     * @throws IOException if a network or stream error occurs
     */
    public String fetchAPIResponse() throws IOException {
        StringBuilder response = new StringBuilder();

        try (Socket socket = createSocket(getHost(), getPort())) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("GET " + getPath() + " HTTP/1.1");
            out.println("Host: " + getHost());
            out.println("Connection: close");
            out.println();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),java.nio.charset.StandardCharsets.UTF_8));

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
