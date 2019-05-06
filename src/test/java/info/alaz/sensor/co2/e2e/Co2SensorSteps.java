package info.alaz.sensor.co2.e2e;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.entity.SensorStatus;
import info.alaz.sensor.co2.web.rest.Co2SensorController;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static info.alaz.sensor.co2.web.rest.ParentCo2SensorController.BASE_API_PATH;
import static info.alaz.sensor.co2.web.rest.ParentCo2SensorController.CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE;
import static org.hamcrest.Matchers.equalTo;

public class Co2SensorSteps {


    private Response response;

    public void setup(int port) {
        RestAssured.reset();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBasePath(BASE_API_PATH)
                .setPort(port)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Step("Create measurement on existing sensor")
    public void createSensorMeasurementsOnSensor(UUID sensorUuid, MeasurementRequestDto measurementRequestDto) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(Co2SensorController.SENSOR_UUID, sensorUuid)
                .body(measurementRequestDto)
                .when()
                .post("/{" + Co2SensorController.SENSOR_UUID + "}/measurements");
    }

    @Step("Fetch status of existing sensor")
    public void getSensorStatus(UUID sensorUuid) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(Co2SensorController.SENSOR_UUID, sensorUuid)
                .when()
                .get("/{" + Co2SensorController.SENSOR_UUID + "}");
    }

    @Step("Fetch sensor metrics")
    public void getSensorMetrics(UUID sensorUuid) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(Co2SensorController.SENSOR_UUID, sensorUuid)
                .when()
                .get("/{" + Co2SensorController.SENSOR_UUID + "}/metrics");
    }

    @Step("List sensor alerts")
    public void listAlerts(UUID sensorUuid) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(Co2SensorController.SENSOR_UUID, sensorUuid)
                .when()
                .get("/{" + Co2SensorController.SENSOR_UUID + "}/alerts");
    }

    @Step("Validating Http status")
    public void validateHttpStatus(HttpStatus httpStatus) {
        response.then().statusCode(httpStatus.value());
        response.prettyPrint();
    }

    @Step("Validating sensor status")
    public void validateSensorStatus(SensorStatus sensorStatus) {
        response.then()
                .body(equalTo("\"" + sensorStatus.name() + "\""));
    }

    @Step("Validating sensor metrics")
    public void validateSensorMetrics(int maxCo2Level, int averageCo2Level) {
        response.then()
                .body("maxLast30Days", equalTo(maxCo2Level))
                .body("avgLast30Days", equalTo(averageCo2Level));
    }

    @Step("Validating alert list")
    public void validateAlertList() {
        response.then()
                .body("[0].startTime", equalTo("2019-04-21T20:05:15+0200"))
                .body("[0].endTime", equalTo("2019-04-21T22:05:15+0200"))
                .body("[0].measurement1", equalTo(2200))
                .body("[0].measurement2", equalTo(2300))
                .body("[0].measurement3", equalTo(2400))
                .body("[1].startTime", equalTo("2019-04-21T19:05:15+0200"))
                .body("[1].endTime", equalTo("2019-04-21T21:05:15+0200"))
                .body("[1].measurement1", equalTo(2100))
                .body("[1].measurement2", equalTo(2200))
                .body("[1].measurement3", equalTo(2300));
        response.prettyPrint();
    }
}
