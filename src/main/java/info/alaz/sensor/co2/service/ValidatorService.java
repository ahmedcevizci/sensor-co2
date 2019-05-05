package info.alaz.sensor.co2.service;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.exception.InvalidCo2MeasurementException;

import java.util.UUID;

public interface ValidatorService {
    void validateMeasurement(UUID sensorUuid, MeasurementRequestDto measurementRequestDto) throws InvalidCo2MeasurementException;
}
