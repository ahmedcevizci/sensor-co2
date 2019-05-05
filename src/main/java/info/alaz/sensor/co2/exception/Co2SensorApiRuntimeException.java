package info.alaz.sensor.co2.exception;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Co2SensorApiRuntimeException extends RuntimeException {

    public Co2SensorApiRuntimeException(String message) {
        super(message);
    }

    public Co2SensorApiRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
