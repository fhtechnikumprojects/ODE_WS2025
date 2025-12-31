package org.example.project_wobimich.model;

import java.time.LocalDateTime;

public class LocationLogEntry {

    private String location;
    private LocalDateTime timestamp;

    public LocationLogEntry(String location) {
    }

    public LocationLogEntry(String location, LocalDateTime timestamp) {
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}