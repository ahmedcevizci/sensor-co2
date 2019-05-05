package info.alaz.sensor.co2.exception;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Co2SensorApiException extends Exception {


    public Co2SensorApiException(String message) {
        super(message);
    }

    public Co2SensorApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
