package info.alaz.sensor.co2.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtil {

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    // If it is already midnight, get exact days ago. However if it is already past midnight, get days-1 ago.
    public static ZonedDateTime getMidnightOfDaysAgo(int days) {
        LocalDate localNow = LocalDate.now();
        ZonedDateTime startOfToday = localNow.atStartOfDay(ZoneId.systemDefault());

        if (localNow.atStartOfDay().equals(localNow)) {
            //If it is already midnight
            return startOfToday.minusDays(days);
        } else {
            return startOfToday.minusDays(days - 1);
        }
    }
}
