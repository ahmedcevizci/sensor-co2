package info.alaz.sensor.co2.service;

import info.alaz.sensor.co2.dto.AlertResponseDto;
import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.dto.MetricsResponseDto;
import info.alaz.sensor.co2.entity.SensorStatus;
import info.alaz.sensor.co2.exception.SensorNotFoundException;

import java.util.List;
import java.util.UUID;

public interface Co2SensorService {

    void saveMeasurement(UUID sensorUuid, MeasurementRequestDto measurementRequestDto) throws SensorNotFoundException;

    SensorStatus getSensorStatus(UUID sensorUuid) throws SensorNotFoundException;

    MetricsResponseDto getSensorMetrics(UUID sensorUuid) throws SensorNotFoundException;

    List<AlertResponseDto> getSensorAlerts(UUID sensorUuid) throws SensorNotFoundException;

}
