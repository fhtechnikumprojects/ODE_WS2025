package org.example.project_wobimich.utils;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LineStationUtils {

    public static String getTimeInMin(String timeStamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        OffsetTime depTimeStamp = OffsetTime.parse(timeStamp, formatter);
        OffsetTime now = OffsetTime.now();
        long minutes = ChronoUnit.MINUTES.between(now, depTimeStamp);

        return "" + minutes;
    }

}
