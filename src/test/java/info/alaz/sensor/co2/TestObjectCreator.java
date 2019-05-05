package info.alaz.sensor.co2;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public final class TestObjectCreator {

    public static final UUID EXISTING_SENSOR_ID = UUID.fromString("0ed88e2d-e7ed-4840-a42a-c1641d3936e0");
    public static final MeasurementRequestDto NEW_NORMAL_MEASUREMENT = new MeasurementRequestDto(1900, ZonedDateTime.of(2019, 5, 5, 22, 0, 0, 0, ZoneId.systemDefault()));
    public static final MeasurementRequestDto NEW_ALARMING_MEASUREMENT = new MeasurementRequestDto(2200, ZonedDateTime.of(2019, 5, 5, 22, 0, 0, 0, ZoneId.systemDefault()));
}
