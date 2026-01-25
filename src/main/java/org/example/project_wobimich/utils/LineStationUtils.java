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

}
