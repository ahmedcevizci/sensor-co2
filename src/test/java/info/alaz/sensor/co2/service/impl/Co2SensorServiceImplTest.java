package info.alaz.sensor.co2.service.impl;

import info.alaz.sensor.co2.config.Co2SensorProperties;
import info.alaz.sensor.co2.mapper.AlertMapper;
import info.alaz.sensor.co2.mapper.MeasurementMapper;
import info.alaz.sensor.co2.repository.AlertRepository;
import info.alaz.sensor.co2.repository.MeasurementRepository;
import info.alaz.sensor.co2.repository.SensorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { Co2SensorServiceImpl.class })
public class Co2SensorServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private MeasurementMapper measurementMapper;

    @Mock
    private AlertMapper alertMapper;

    @Spy
    private Co2SensorProperties co2SensorProperties = new Co2SensorProperties(2000);


//    @Captor
//    private ArgumentCaptor<?> sArgumentCaptor;

    @InjectMocks
    private Co2SensorServiceImpl co2SensorService;

    @Test
    public void saveMeasurement() {
    }

    @Test
    public void getSensorStatus() {
    }

    @Test
    public void getSensorMetrics() {
    }

    @Test
    public void getSensorAlerts() {
    }
}