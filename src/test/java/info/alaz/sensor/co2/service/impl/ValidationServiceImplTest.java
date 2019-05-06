package info.alaz.sensor.co2.service.impl;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.exception.Co2LevelCannotBeNegativeException;
import info.alaz.sensor.co2.exception.InvalidCo2MeasurementException;
import info.alaz.sensor.co2.exception.MeasurementTimeCannotBeInFutureException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {ValidationServiceImpl.class})
public class ValidationServiceImplTest {

    @InjectMocks
    private ValidationServiceImpl validationService;

    @Test
    public void validateMeasurement_GivenValidMeasurementDto_ShoulPass() throws InvalidCo2MeasurementException {
        //Given
        UUID sensorUuid = UUID.randomUUID();
        MeasurementRequestDto measurementRequestDto = new MeasurementRequestDto(2000, ZonedDateTime.now());

        validationService.validateMeasurement(sensorUuid, measurementRequestDto);
    }

    @Test(expected = MeasurementTimeCannotBeInFutureException.class)
    public void validateMeasurement_GivenFeatureMeasurementTime_ShoulThrow() throws InvalidCo2MeasurementException {
        //Given
        UUID sensorUuid = UUID.randomUUID();
        MeasurementRequestDto measurementRequestDto = new MeasurementRequestDto(2000, ZonedDateTime.now().plusDays(2));

        validationService.validateMeasurement(sensorUuid, measurementRequestDto);
    }

    @Test(expected = Co2LevelCannotBeNegativeException.class)
    public void validateMeasurement_GivenNegativeCo2Level_ShoulThrow() throws InvalidCo2MeasurementException {
        //Given
        UUID sensorUuid = UUID.randomUUID();
        MeasurementRequestDto measurementRequestDto = new MeasurementRequestDto(-2000, ZonedDateTime.now());

        validationService.validateMeasurement(sensorUuid, measurementRequestDto);
    }

    @Test
    public void validateMeasurement_GivenZeroCo2Level_ShouldPass() throws InvalidCo2MeasurementException {
        //Given
        UUID sensorUuid = UUID.randomUUID();
        MeasurementRequestDto measurementRequestDto = new MeasurementRequestDto(0, ZonedDateTime.now());

        validationService.validateMeasurement(sensorUuid, measurementRequestDto);
    }


    @Test(expected = InvalidCo2MeasurementException.class)
    public void validateMeasurement_GivenNullCo2Level_ShoulThrow() throws InvalidCo2MeasurementException {
        //Given
        UUID sensorUuid = UUID.randomUUID();
        MeasurementRequestDto measurementRequestDto = new MeasurementRequestDto(null, ZonedDateTime.now());

        validationService.validateMeasurement(sensorUuid, measurementRequestDto);
    }

    @Test(expected = InvalidCo2MeasurementException.class)
    public void validateMeasurement_GivenNullMeasurementDate_ShoulThrow() throws InvalidCo2MeasurementException {
        //Given
        UUID sensorUuid = UUID.randomUUID();
        MeasurementRequestDto measurementRequestDto = new MeasurementRequestDto(2000, null);

        validationService.validateMeasurement(sensorUuid, measurementRequestDto);
    }
}