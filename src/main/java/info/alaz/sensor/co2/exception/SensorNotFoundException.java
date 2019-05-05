package info.alaz.sensor.co2.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SensorNotFoundException extends Co2SensorApiException {

    public static final String DEFAULT_MESSAGE = "Sensor can be found for given UUID: %s";

    private final UUID sensorUuid;

    public SensorNotFoundException(String message, UUID sensorUuid) {
        super(message);
        this.sensorUuid = sensorUuid;
    }

    public SensorNotFoundException(String message, Throwable throwable, UUID sensorUuid) {
        super(message, throwable);
        this.sensorUuid = sensorUuid;
    }

    public SensorNotFoundException(UUID sensorUuid) {
        super(String.format(DEFAULT_MESSAGE, sensorUuid.toString()));
        this.sensorUuid = sensorUuid;
    }

    public SensorNotFoundException(Throwable throwable, UUID sensorUuid) {
        super(String.format(DEFAULT_MESSAGE, sensorUuid.toString()), throwable);
        this.sensorUuid = sensorUuid;
    }
}
