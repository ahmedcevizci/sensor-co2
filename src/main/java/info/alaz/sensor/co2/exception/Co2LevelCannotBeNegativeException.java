package info.alaz.sensor.co2.exception;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Co2LevelCannotBeNegativeException extends InvalidCo2MeasurementException {

    public static final String DEFAULT_REASON = "CO2 levels cannot be negative Co2: %d";

    public Co2LevelCannotBeNegativeException(MeasurementRequestDto measurementRequestDto) {
        super(String.format(DEFAULT_REASON, measurementRequestDto.getCo2()), measurementRequestDto);
    }
}
