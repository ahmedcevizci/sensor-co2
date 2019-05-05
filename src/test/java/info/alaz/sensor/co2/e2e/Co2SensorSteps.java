package info.alaz.sensor.co2.e2e;

import info.alaz.sensor.co2.dto.MeasurementRequestDto;
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
import static org.hamcrest.Matchers.is;

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

    @Step("Validating Http status")
    public void validateHttpStatus(HttpStatus httpStatus) {
        response.then().statusCode(httpStatus.value());
    }

 /*   @Step("Sensor metrics are valid for sensor")
    public void validateSensorMetrics(String some, int numberOfElements) {
        response.then()
                .body("content[0].some", is(some))
                .body("content.size()", is(numberOfElements));
    }
*/
}
