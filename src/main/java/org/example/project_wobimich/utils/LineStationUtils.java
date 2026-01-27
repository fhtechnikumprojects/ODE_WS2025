package org.example.project_wobimich.utils;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LineStationUtils {

    /**
     * Calculates the remaining minutes until the given departure time.
     * <p>
     * The result may be negative if the departure time is already past.
     *
     * @param departureTime ISO-like timestamp string including timezone offset
     * @return remaining time in minutes as string
     */
    public static String getTimeInMin(String departureTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        OffsetTime depTimeStamp = OffsetTime.parse(departureTime, formatter);
        OffsetTime currentTime = OffsetTime.now();
        long minutes = ChronoUnit.MINUTES.between(currentTime, depTimeStamp);

        return "" + minutes;
    }

    /**
     * Calculates the remaining minutes until the given departure time.
     * <p>
     * Uses the existing getTimeInMin() method internally and ensures
     * that negative values are capped at 0, so departures in the past
     * are reported as 0 minutes.
     *
     * @param departureTime ISO-like timestamp string including timezone offset
     * @return remaining minutes until departure as an int (minimum 0)
     */
    public static int getMinutesUntilDeparture(String departureTime) {
        try {
            int minutes = Integer.parseInt(getTimeInMin(departureTime));
            return Math.max(minutes, 0);
        } catch (NumberFormatException e) {
            System.err.println("Fehler beim Parsen der Minuten: " + departureTime);
            return 0;
        }
    }

}
