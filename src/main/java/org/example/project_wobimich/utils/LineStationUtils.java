package org.example.project_wobimich.utils;

import org.example.project_wobimich.model.LineStation;
import java.util.List;

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
     *
     * if departureTime is set on next day, correction by if methode;
     * @return remaining time in minutes as string
     */
    public static String getTimeInMin(String departureTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        OffsetTime depTimeStamp = OffsetTime.parse(departureTime, formatter);
        OffsetTime currentTime = OffsetTime.now();
        long minutes = ChronoUnit.MINUTES.between(currentTime, depTimeStamp);
        if (minutes < 0) {minutes = 1440 + minutes;}

        return "" + minutes;
    }

    /**
     * Updates the countdown in minutes for the given list of LineStation objects.
     * <p>
     * For each LineStation in the list, it calculates the remaining time in minutes
     * until the next departure time.
     *
     * @param lines a list of LineStation objects for which the countdown in minutes
     *              needs to be updated; can be null or empty. Individual LineStation
     *              objects or their departureTimes property can also be null.
     */
    public static void updateCountdownMinutes(List<LineStation> lines) {
        if (lines == null) return;
        for (LineStation l : lines) {
            if (l == null || l.getDepartureTimes() == null || l.getDepartureTimes().isEmpty()) continue;

            //String planned = l.getDepartureTimes().get(0);
            //String minutes = getTimeInMin(planned);

        }

    }
}
