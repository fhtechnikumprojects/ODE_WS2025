package org.example.project_wobimich.api;

/**
 * Custom exception used to signal errors during API communication
 * or API response processing.
 */
public class ApiException extends Exception {
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
