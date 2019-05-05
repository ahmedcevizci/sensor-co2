package info.alaz.sensor.co2.exception;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

import static info.alaz.sensor.co2.util.DateUtil.DATE_FORMAT_PATTERN;

@Getter
@EqualsAndHashCode(callSuper = true)
public class MeasurementTimeCannotBeInFutureException extends InvalidCo2MeasurementException {

    public static final String DEFAULT_REASON = "Measurement time cannot be in future: %s";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    public MeasurementTimeCannotBeInFutureException(MeasurementRequestDto measurementRequestDto) {
        super(String.format(DEFAULT_REASON, DATE_TIME_FORMATTER.format(measurementRequestDto.getTime())), measurementRequestDto);
    }
}
