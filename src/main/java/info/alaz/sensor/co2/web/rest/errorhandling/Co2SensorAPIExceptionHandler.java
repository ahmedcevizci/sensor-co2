package info.alaz.sensor.co2.web.rest.errorhandling;


import info.alaz.sensor.co2.constant.APIErrorHeaders;
import info.alaz.sensor.co2.exception.Co2LevelCannotBeNegativeException;
import info.alaz.sensor.co2.exception.InvalidCo2MeasurementException;
import info.alaz.sensor.co2.exception.MeasurementTimeCannotBeInFutureException;
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
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CO2SensorAPI-error", "error." + APIErrorHeaders.SENSOR_NOT_FOUND);
        return createResponse(HttpStatus.NOT_FOUND, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(final Co2LevelCannotBeNegativeException ex) {
        logger.warn("CO2 SensorEntity Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CO2SensorAPI-error", "error." + APIErrorHeaders.CO2_LEVEL_CANNOT_BE_NEGATIVE);
        return createResponse(HttpStatus.BAD_REQUEST, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(final InvalidCo2MeasurementException ex) {
        logger.warn("CO2 SensorEntity Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CO2SensorAPI-error", "error." + APIErrorHeaders.INVALID_CO2_LEVEL);
        return createResponse(HttpStatus.BAD_REQUEST, headers, ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequest(final MeasurementTimeCannotBeInFutureException ex) {
        logger.warn("CO2 SensorEntity Api Exception", ex.getMessage(), this.getClass().getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-CO2SensorAPI-error", "error." + APIErrorHeaders.MEASUREMENT_CANNOT_BE_IN_FUTURE);
        return createResponse(HttpStatus.BAD_REQUEST, headers, ex.getMessage());
    }
}
