package info.alaz.sensor.co2.service.impl;

import info.alaz.sensor.co2.config.Co2SensorProperties;
import info.alaz.sensor.co2.dto.AlertResponseDto;
import info.alaz.sensor.co2.dto.MetricsResponseDto;
import info.alaz.sensor.co2.entity.AlertEntity;
import info.alaz.sensor.co2.entity.MeasurementEntity;
import info.alaz.sensor.co2.entity.SensorEntity;
import info.alaz.sensor.co2.entity.SensorStatus;
import info.alaz.sensor.co2.exception.SensorNotFoundException;
import info.alaz.sensor.co2.mapper.AlertMapper;
import info.alaz.sensor.co2.mapper.AlertMapperImpl;
import info.alaz.sensor.co2.mapper.MeasurementMapper;
import info.alaz.sensor.co2.mapper.MeasurementMapperImpl;
import info.alaz.sensor.co2.repository.AlertRepository;
import info.alaz.sensor.co2.repository.MeasurementRepository;
import info.alaz.sensor.co2.repository.SensorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static info.alaz.sensor.co2.TestObjectCreator.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {Co2SensorServiceImpl.class})
public class Co2SensorServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private AlertRepository alertRepository;

    @Spy
    private MeasurementMapper measurementMapper = new MeasurementMapperImpl();

    @Spy
    private AlertMapper alertMapper = new AlertMapperImpl();

    @Spy
    private Co2SensorProperties co2SensorProperties = new Co2SensorProperties(2000);

    @Captor
    private ArgumentCaptor<MeasurementEntity> measurementEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<SensorEntity> sensorEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<AlertEntity> alertEntityArgumentCaptor;

    @InjectMocks
    private Co2SensorServiceImpl co2SensorService;

    @Test
    public void saveMeasurement_GivenExistingSensorWithOkStatus_And_GivenNormalMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.OK, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_NORMAL_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);
        verify(measurementMapper, times(1)).toEntity(null, NEW_NORMAL_MEASUREMENT, sensorEntity);
        verify(measurementRepository, times(0)).findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID);

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_NORMAL_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_NORMAL_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithOkStatus_And_GivenAlertMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.OK, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_ALARMING_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);

        verify(sensorRepository, times(1)).save(sensorEntityArgumentCaptor.capture());
        assertEquals(SensorStatus.WARN, sensorEntityArgumentCaptor.getValue().getStatus());

        verify(measurementMapper, times(1)).toEntity(null, NEW_ALARMING_MEASUREMENT, sensorEntity);
        verify(measurementRepository, times(0)).findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID);

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_ALARMING_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_ALARMING_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithWarnStatus_And_GivenNormalMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.WARN, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_NORMAL_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);
        verify(measurementMapper, times(1)).toEntity(null, NEW_NORMAL_MEASUREMENT, sensorEntity);

        verify(sensorRepository, times(1)).save(sensorEntityArgumentCaptor.capture());
        assertEquals(SensorStatus.OK, sensorEntityArgumentCaptor.getValue().getStatus());

        verify(measurementRepository, times(0)).findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID);

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_NORMAL_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_NORMAL_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithWarnStatus_And_GivenAlertMeasurement_And_GivenPreviousAlertingMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.WARN, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);
        when(measurementRepository.findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID)).thenReturn(createAlertingMeasurementEntityList(sensorEntity));

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_ALARMING_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);

        verify(sensorRepository, times(1)).save(sensorEntityArgumentCaptor.capture());
        assertEquals(SensorStatus.ALERT, sensorEntityArgumentCaptor.getValue().getStatus());

        verify(measurementMapper, times(1)).toEntity(null, NEW_ALARMING_MEASUREMENT, sensorEntity);
        verify(measurementRepository, times(1)).findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID);


        verify(alertRepository, times(1)).save(alertEntityArgumentCaptor.capture());
        assertEquals(NEW_ALARMING_MEASUREMENT.getCo2(), alertEntityArgumentCaptor.getValue().getThirdCo2Level());
        assertEquals(NEW_ALARMING_MEASUREMENT.getTime(), alertEntityArgumentCaptor.getValue().getDateEnded());

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_ALARMING_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_ALARMING_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithWarnStatus_And_GivenAlertMeasurement_And_GivenPreviousNormalMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.WARN, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);
        when(measurementRepository.findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID)).thenReturn(createWarningMeasurementEntityList(sensorEntity));

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_ALARMING_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);

        verify(measurementMapper, times(1)).toEntity(null, NEW_ALARMING_MEASUREMENT, sensorEntity);
        verify(measurementRepository, times(1)).findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID);

        verify(sensorRepository, times(0)).save(any(SensorEntity.class));
        verify(alertRepository, times(0)).save(any(AlertEntity.class));

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_ALARMING_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_ALARMING_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithAlertStatus_And_GivenAlertingMeasurement_And_GivenPreviousAlertingMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.ALERT, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);
        when(measurementRepository.findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID)).thenReturn(createAlertingMeasurementEntityList(sensorEntity));

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_ALARMING_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);

        verify(measurementMapper, times(1)).toEntity(null, NEW_ALARMING_MEASUREMENT, sensorEntity);

        verify(sensorRepository, times(0)).save(any(SensorEntity.class));

        verify(alertRepository, times(1)).save(alertEntityArgumentCaptor.capture());
        assertEquals(NEW_ALARMING_MEASUREMENT.getCo2(), alertEntityArgumentCaptor.getValue().getThirdCo2Level());
        assertEquals(NEW_ALARMING_MEASUREMENT.getTime(), alertEntityArgumentCaptor.getValue().getDateEnded());

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_ALARMING_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_ALARMING_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithAlertStatus_And_GivenNormalMeasurement_And_GivenPreviousAlertingMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.ALERT, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);
        when(measurementRepository.findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID)).thenReturn(createAlertingMeasurementEntityList(sensorEntity));

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_NORMAL_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);

        verify(measurementMapper, times(1)).toEntity(null, NEW_NORMAL_MEASUREMENT, sensorEntity);

        verify(sensorRepository, times(0)).save(any(SensorEntity.class));

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_NORMAL_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_NORMAL_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void saveMeasurement_GivenExistingSensorWithAlertStatus_And_GivenNormalMeasurement_And_GivenPreviousNormalMeasurement_ShouldPass() throws SensorNotFoundException {
        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.ALERT, dateStatusChanged, dateCreated, null);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);
        when(measurementRepository.findTop2BySensor_UuidOrderByDateMeasuredDesc(EXISTING_OK_SENSOR_ID)).thenReturn(createNormalMeasurementEntityList(sensorEntity));

        //When
        co2SensorService.saveMeasurement(EXISTING_OK_SENSOR_ID, NEW_NORMAL_MEASUREMENT);

        //Then
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);

        verify(measurementMapper, times(1)).toEntity(null, NEW_NORMAL_MEASUREMENT, sensorEntity);

        verify(sensorRepository, times(1)).save(sensorEntityArgumentCaptor.capture());
        assertEquals(SensorStatus.OK, sensorEntityArgumentCaptor.getValue().getStatus());

        verify(measurementRepository, times(1)).save(measurementEntityArgumentCaptor.capture());
        assertEquals(NEW_NORMAL_MEASUREMENT.getCo2(), measurementEntityArgumentCaptor.getValue().getCo2Level());
        assertEquals(NEW_NORMAL_MEASUREMENT.getTime(), measurementEntityArgumentCaptor.getValue().getDateMeasured());
    }

    @Test
    public void getSensorStatus_GivenExistingSensor_ShouldPass() throws SensorNotFoundException {
        //Given
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(new SensorEntity(SensorStatus.OK, null, null, null));

        //When
        SensorStatus sensorStatus = co2SensorService.getSensorStatus(EXISTING_OK_SENSOR_ID);

        //Then
        assertEquals(SensorStatus.OK, sensorStatus);
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);
    }

    @Test(expected = SensorNotFoundException.class)
    public void getSensorStatus_GivenNotExistingSensor_ShouldPass() throws SensorNotFoundException {
        //Given
        when(sensorRepository.findOne(UUID.randomUUID())).thenReturn(null);

        //When
        co2SensorService.getSensorStatus(EXISTING_OK_SENSOR_ID);

        //Then
    }

    @Test
    public void getSensorMetrics_GivenExistingSensor_ShouldPass() throws SensorNotFoundException {
        //Given
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(new SensorEntity(SensorStatus.OK, null, null, null));
        when(measurementRepository.getMaxCo2LevelOfSensorAfterGivenDate(eq(EXISTING_OK_SENSOR_ID), any(ZonedDateTime.class))).thenReturn(2100);
        when(measurementRepository.getAverageCo2LevelOfSensorAfterGivenDate(eq(EXISTING_OK_SENSOR_ID), any(ZonedDateTime.class))).thenReturn(1700);

        //When
        MetricsResponseDto metricsResponseDto = co2SensorService.getSensorMetrics(EXISTING_OK_SENSOR_ID);

        //Then
        assertEquals(new Integer(2100), metricsResponseDto.getMaxLast30Days());
        assertEquals(new Integer(1700), metricsResponseDto.getAvgLast30Days());
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);
        verify(measurementRepository, times(1)).getMaxCo2LevelOfSensorAfterGivenDate(eq(EXISTING_OK_SENSOR_ID), any(ZonedDateTime.class));
        verify(measurementRepository, times(1)).getAverageCo2LevelOfSensorAfterGivenDate(eq(EXISTING_OK_SENSOR_ID), any(ZonedDateTime.class));
    }

    @Test(expected = SensorNotFoundException.class)
    public void getSensorMetrics_GivenNotExistingSensor_ShouldPass() throws SensorNotFoundException {
        //Given
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(null);

        //When
        co2SensorService.getSensorMetrics(EXISTING_OK_SENSOR_ID);

        //Then
    }

    @Test
    public void getSensorAlerts_GivenExistingSensor_ShouldPass() throws SensorNotFoundException {
        //Given
        SensorEntity sensorEntity = new SensorEntity(SensorStatus.ALERT, null, null, null);
        ZonedDateTime endDate = ZonedDateTime.now();
        ZonedDateTime startDate = endDate.minusDays(2);

        AlertEntity alertEntity = new AlertEntity(sensorEntity, 2100, 2200, 2300, startDate, endDate);
        List<AlertEntity> alertEntityList = new ArrayList<>(1);
        alertEntityList.add(alertEntity);
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(sensorEntity);
        when(alertRepository.findAllBySensor_UuidOrderByDateEndedDesc(EXISTING_OK_SENSOR_ID)).thenReturn(alertEntityList);

        //When
        List<AlertResponseDto> alertResponseDtoList = co2SensorService.getSensorAlerts(EXISTING_OK_SENSOR_ID);

        //Then
        assertEquals(new Integer(2100), alertResponseDtoList.get(0).getMeasurement1());
        assertEquals(new Integer(2200), alertResponseDtoList.get(0).getMeasurement2());
        assertEquals(new Integer(2300), alertResponseDtoList.get(0).getMeasurement3());
        assertEquals(new Integer(2300), alertResponseDtoList.get(0).getMeasurement3());
        assertEquals(startDate, alertResponseDtoList.get(0).getStartTime());
        assertEquals(endDate, alertResponseDtoList.get(0).getEndTime());
        verify(sensorRepository, times(1)).findOne(EXISTING_OK_SENSOR_ID);
        verify(alertRepository, times(1)).findAllBySensor_UuidOrderByDateEndedDesc(eq(EXISTING_OK_SENSOR_ID));
        verify(alertMapper, times(1)).toDtoList(eq(alertEntityList));
    }

    @Test(expected = SensorNotFoundException.class)
    public void getSensorAlerts_GivenNotExistingSensor_ShouldThrow() throws SensorNotFoundException {
        //Given
        when(sensorRepository.findOne(EXISTING_OK_SENSOR_ID)).thenReturn(null);

        //When
        co2SensorService.getSensorAlerts(EXISTING_OK_SENSOR_ID);

        //Then
    }
}