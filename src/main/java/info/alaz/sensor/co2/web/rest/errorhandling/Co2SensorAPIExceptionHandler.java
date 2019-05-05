package info.alaz.sensor.co2.web.rest.errorhandling;


import info.alaz.sensor.co2.constant.APIErrorHeaders;
import info.alaz.sensor.co2.exception.SensorNotFoundException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@NoArgsConstructor
@RestControllerAdvice
public class Co2SensorAPIExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(Co2SensorAPIExceptionHandler.class);

    protected ResponseEntity<ErrorResponse> createResponse(HttpStatus status, HttpHeaders headers, String reason) {
        ErrorResponse error = new ErrorResponse(status.value(), reason);
        return new ResponseEntity(error, headers, status);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundRequest(final SensorNotFoundException ex) {
        logger.warn("CO2 SensorEntity Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = createFailureAlert("Entity", APIErrorHeaders.REQUEST_BODY_EMPTY);
        return createResponse(HttpStatus.NOT_FOUND, headers, ex.getMessage());
    }


    // TODO Advice other exceptions as well

    private static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CO2SensorAPI-alert", message);
        headers.add("X-CO2SensorAPI-params", param);
        return headers;
    }

    public static HttpHeaders createEntityGetAlert(String entityName, String param) {
        return createAlert("CO2SensorAPI." + entityName + ".get", param);
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("CO2SensorAPI." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("CO2SensorAPI." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("CO2SensorAPI." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CO2SensorAPI-error", "error." + errorKey);
        headers.add("X-CO2SensorAPI-params", entityName);
        return headers;
    }

}
