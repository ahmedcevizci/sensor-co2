package info.alaz.sensor.co2.e2e;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.alaz.sensor.co2.TestObjectCreator;
import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.entity.SensorStatus;
import info.alaz.sensor.co2.util.DateUtil;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@RunWith(SpringIntegrationSerenityRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
public class Co2SensorApiE2e {

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("postgresql-db", 5432);

    private static ObjectMapper objectMapper;

    @Steps
    private Co2SensorSteps co2SensorSteps;

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void setupClass() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.DATE_FORMAT_PATTERN));
    }

    @Before
    public void setup() {
        co2SensorSteps.setup(port);
    }

    public static <T> String getDtoAsJson(T dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_create_a_new_normal_measurement_on_existing_sensor_which_has_no_previous_measurement() throws Exception {
        //Given

        //When
        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_OK_SENSOR_ID, TestObjectCreator.NEW_NORMAL_MEASUREMENT);

        //Then
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_create_three_alerting_measurement_on_existing_ok_sensor_and_change_its_state_to_alert() throws Exception {

        MeasurementRequestDto firstMeasurement = new MeasurementRequestDto(2200, ZonedDateTime.of(2019, 5, 5, 20, 0, 0, 0, ZoneId.systemDefault()));
        MeasurementRequestDto secondMeasurement = new MeasurementRequestDto(2300, ZonedDateTime.of(2019, 5, 5, 21, 0, 0, 0, ZoneId.systemDefault()));
        MeasurementRequestDto thirdMeasurement = new MeasurementRequestDto(2400, ZonedDateTime.of(2019, 5, 5, 22, 0, 0, 0, ZoneId.systemDefault()));

        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_OK_SENSOR_ID2, firstMeasurement);
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);

        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_OK_SENSOR_ID2);
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.WARN);


        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_OK_SENSOR_ID2, secondMeasurement);
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);

        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_OK_SENSOR_ID2);
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.WARN);

        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_OK_SENSOR_ID2, thirdMeasurement);
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);

        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_OK_SENSOR_ID2);
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.ALERT);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_create_three_normal_measurement_on_existing_alerting_sensor_and_change_its_state_to_ok() throws Exception {

        MeasurementRequestDto firstMeasurement = new MeasurementRequestDto(1900, ZonedDateTime.of(2019, 5, 5, 20, 0, 0, 0, ZoneId.systemDefault()));
        MeasurementRequestDto secondMeasurement = new MeasurementRequestDto(1800, ZonedDateTime.of(2019, 5, 5, 21, 0, 0, 0, ZoneId.systemDefault()));
        MeasurementRequestDto thirdMeasurement = new MeasurementRequestDto(1700, ZonedDateTime.of(2019, 5, 5, 22, 0, 0, 0, ZoneId.systemDefault()));

        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_ALERT_SENSOR_ID, firstMeasurement);
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);

        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_ALERT_SENSOR_ID);
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.ALERT);


        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_ALERT_SENSOR_ID, secondMeasurement);
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);

        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_ALERT_SENSOR_ID);
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.ALERT);

        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_ALERT_SENSOR_ID, thirdMeasurement);
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);

        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_ALERT_SENSOR_ID);
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.OK);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_create_a_new_alarming_measurement_on_existing_sensor_which_has_no_previous_measurement() throws Exception {
        //Given

        //When
        co2SensorSteps.createSensorMeasurementsOnSensor(TestObjectCreator.EXISTING_OK_SENSOR_ID, TestObjectCreator.NEW_ALARMING_MEASUREMENT);

        //Then
        co2SensorSteps.validateHttpStatus(HttpStatus.CREATED);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_fetch_status_on_existing_sensor_which_has_no_previous_measurement() throws Exception {
        //Given

        //When
        co2SensorSteps.getSensorStatus(TestObjectCreator.EXISTING_OK_SENSOR_ID);

        //Then
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorStatus(SensorStatus.OK);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_fetch_metrics_on_alerting_existing_sensor_which_has_previous_measurements() throws Exception {
        //Given

        //When
        co2SensorSteps.getSensorMetrics(TestObjectCreator.EXISTING_ALERT_SENSOR_ID);

        //Then
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateSensorMetrics(2400, 1800);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_list_alerts_on_alerting_existing_sensor_which_has_previous_measurements() throws Exception {
        //Given

        //When
        co2SensorSteps.listAlerts(TestObjectCreator.EXISTING_ALERT_SENSOR_ID);

        //Then
        co2SensorSteps.validateHttpStatus(HttpStatus.OK);
        co2SensorSteps.validateAlertList();
    }
}
