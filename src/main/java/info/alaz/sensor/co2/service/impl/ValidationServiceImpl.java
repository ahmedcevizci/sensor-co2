package info.alaz.sensor.co2.service.impl;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.exception.Co2LevelCannotBeNegativeException;
import info.alaz.sensor.co2.exception.InvalidCo2MeasurementException;
import info.alaz.sensor.co2.exception.MeasurementTimeCannotBeInFutureException;
import info.alaz.sensor.co2.service.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class ValidationServiceImpl implements ValidatorService {

    private Logger logger = LoggerFactory.getLogger(ValidationServiceImpl.class);

    @Override
    public void validateMeasurement(UUID sensorUuid, MeasurementRequestDto measurementRequestDto) throws InvalidCo2MeasurementException {
        logger.debug("Validating measurement");
        Integer co2Level = measurementRequestDto.getCo2();
        if (co2Level == null) {
            throw new InvalidCo2MeasurementException("CO2 level cannot be null", measurementRequestDto);
        }
        if (co2Level < 0) {
            throw new Co2LevelCannotBeNegativeException(measurementRequestDto);
        }

        ZonedDateTime measurementTime = measurementRequestDto.getTime();
        if (measurementTime == null) {
            throw new InvalidCo2MeasurementException("Measurement time cannot be null", measurementRequestDto);
        }
        if (measurementTime.isAfter(ZonedDateTime.now())) {
            throw new MeasurementTimeCannotBeInFutureException(measurementRequestDto);
        }
        logger.debug("Validated measurement");
    }
}
