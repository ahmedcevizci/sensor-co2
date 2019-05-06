package info.alaz.sensor.co2;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.entity.MeasurementEntity;
import info.alaz.sensor.co2.entity.SensorEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TestObjectCreator {

    public static final UUID EXISTING_OK_SENSOR_ID = UUID.fromString("0ed88e2d-e7ed-4840-a42a-c1641d3936e0");
    public static final UUID EXISTING_OK_SENSOR_ID2 = UUID.fromString("a981fc48-332b-46d8-8b22-c5eb8e02cc43");
    public static final UUID EXISTING_WARN_SENSOR_ID = UUID.fromString("3f01795c-2b80-4e90-8065-0767a11588ed");
    public static final UUID EXISTING_ALERT_SENSOR_ID = UUID.fromString("920dddec-0900-4f8d-b4f1-a05ddeaa4159");
    public static final UUID NON_EXISTING_SENSOR_ID = UUID.fromString("1ddce4a6-64d9-4e29-bfe1-f964127f8648");
    public static final MeasurementRequestDto NEW_NORMAL_MEASUREMENT = new MeasurementRequestDto(1900, ZonedDateTime.of(2019, 5, 5, 22, 0, 0, 0, ZoneId.systemDefault()));
    public static final MeasurementRequestDto NEW_ALARMING_MEASUREMENT = new MeasurementRequestDto(2200, ZonedDateTime.of(2019, 5, 5, 22, 0, 0, 0, ZoneId.systemDefault()));

    public static List<MeasurementEntity> createAlertingMeasurementEntityList(SensorEntity sensorEntity) {
        MeasurementEntity firstMeasurement = new MeasurementEntity(sensorEntity, 2200, ZonedDateTime.now());
        MeasurementEntity secondMeasurement = new MeasurementEntity(sensorEntity, 2400, ZonedDateTime.now());
        List<MeasurementEntity> measurementEntityList = new ArrayList<>();
        measurementEntityList.add(firstMeasurement);
        measurementEntityList.add(secondMeasurement);
        return measurementEntityList;
    }

    public static List<MeasurementEntity> createNormalMeasurementEntityList(SensorEntity sensorEntity) {
        MeasurementEntity firstMeasurement = new MeasurementEntity(sensorEntity, 1700, ZonedDateTime.now());
        MeasurementEntity secondMeasurement = new MeasurementEntity(sensorEntity, 1800, ZonedDateTime.now());
        List<MeasurementEntity> measurementEntityList = new ArrayList<>();
        measurementEntityList.add(firstMeasurement);
        measurementEntityList.add(secondMeasurement);
        return measurementEntityList;
    }

    public static List<MeasurementEntity> createWarningMeasurementEntityList(SensorEntity sensorEntity) {
        MeasurementEntity firstMeasurement = new MeasurementEntity(sensorEntity, 1700, ZonedDateTime.now());
        MeasurementEntity secondMeasurement = new MeasurementEntity(sensorEntity, 2100, ZonedDateTime.now());
        List<MeasurementEntity> measurementEntityList = new ArrayList<>();
        measurementEntityList.add(firstMeasurement);
        measurementEntityList.add(secondMeasurement);
        return measurementEntityList;
    }

}
