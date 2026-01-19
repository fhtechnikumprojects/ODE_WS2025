package org.example.project_wobimich.model;

import java.time.LocalDateTime;

/**
 * Represents a single entry in the location search history.
 * <p>
 * Stores the searched location as a string together with the
 * timestamp when the search was performed.
 */
public class LocationLogEntry {
    private String location;
    private LocalDateTime timestamp;

    /**
     * Creates a new log entry with the current timestamp.
     *
     * @param location the searched location
     */
    public LocationLogEntry(String location) {
        this.location = location;
        this.timestamp = LocalDateTime.now();
    }
}