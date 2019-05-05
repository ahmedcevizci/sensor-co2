package info.alaz.sensor.co2.web.rest;


import info.alaz.sensor.co2.service.Co2SensorService;
import info.alaz.sensor.co2.service.ValidatorService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RestController
@RequestMapping(value = ParentCo2SensorController.BASE_API_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = ParentCo2SensorController.CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE)
public abstract class ParentCo2SensorController {

    public static final String BASE_API_PATH = "/api/v1/sensors";
    public static final String CO2_SENSOR_API_V1_RESPONSE_MEDIA_TYPE = "application/vnd.alaz.info.sensor.co2.v1.response+json";
    public static final String APPLICATION_JSON = "application/json";
    public static final String SWAGGER_VERSIONING = "CO2-SensorEntity-API-V1";

    private Logger logger = LoggerFactory.getLogger(ParentCo2SensorController.class);

    protected final Co2SensorService co2SensorService;
    protected final ValidatorService validatorService;

    public ParentCo2SensorController(Co2SensorService co2SensorService, ValidatorService validatorService) {
        this.co2SensorService = co2SensorService;
        this.validatorService = validatorService;
    }

    @RequestMapping("/")
    public String index() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }
}
