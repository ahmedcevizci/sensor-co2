package info.alaz.sensor.co2.exception;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class InvalidCo2MeasurementException extends Co2SensorApiException {

    public static final String DEFAULT_MSG = "Invalid CO2 measurement: %s";

    private MeasurementRequestDto measurementRequestDto;

    public InvalidCo2MeasurementException(String reason) {
        super(String.format(DEFAULT_MSG, reason));
    }

    public InvalidCo2MeasurementException(String reason, MeasurementRequestDto measurementRequestDto) {
        super(String.format(DEFAULT_MSG, reason));
        this.measurementRequestDto = measurementRequestDto;
    }
}
