package info.alaz.sensor.co2.web.rest.errorhandling;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorResponse {
    private final int code;
    private final String message;
    private String timestamp;

    public ErrorResponse(int code, String message) {
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.code = code;
        this.message = message;
    }

}
