package info.alaz.sensor.co2.web.rest;

import info.alaz.sensor.co2.constant.APIErrorMessages;
import info.alaz.sensor.co2.constant.APITags;
import info.alaz.sensor.co2.dto.AlertResponseDto;
import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.dto.MetricsResponseDto;
import info.alaz.sensor.co2.entity.SensorStatus;
import info.alaz.sensor.co2.service.Co2SensorService;
import info.alaz.sensor.co2.service.ValidatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(tags = {APITags.CO2_SENSOR_API_TAG})
@RestController
public class Co2SensorController extends ParentCo2SensorController {

    public static final String SENSOR_UUID = "sensorUuid";
    private Logger logger = LoggerFactory.getLogger(Co2SensorController.class);

    @Autowired
    public Co2SensorController(Co2SensorService co2SensorService, ValidatorService validatorService) {
        super(co2SensorService, validatorService);
    }

    @ApiOperation(value = "Create sensor measurement", tags = APITags.CO2_SENSOR_API_TAG)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = APIErrorMessages.REQUEST_BODY_EMPTY)
    })
    @ResponseStatus(CREATED)
    @PostMapping(path = "/{" + SENSOR_UUID + "}/measurements")
    public void createSensorMeasurements(@PathVariable(name = SENSOR_UUID) UUID sensorUuid,
                                         @RequestBody MeasurementRequestDto measurementRequestDto) throws Exception {

        logger.info(String.format("Collecting sensor measurement on sensor: %s", sensorUuid.toString()));

        this.validatorService.validateMeasurement(sensorUuid, measurementRequestDto);
        this.co2SensorService.saveMeasurement(sensorUuid, measurementRequestDto);

        logger.info(String.format("Collected sensor measurement on sensor: %s", sensorUuid.toString()));
    }

    @ApiOperation(value = "Get sensor status", tags = APITags.CO2_SENSOR_API_TAG)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = APIErrorMessages.REQUEST_BODY_EMPTY)
    })
    @ResponseStatus(OK)
    @GetMapping(path = "/{" + SENSOR_UUID + "}")
    public SensorStatus getSensorStatus(@PathVariable(name = SENSOR_UUID) UUID sensorUuid) throws Exception {

        logger.info(String.format("Fetching sensor status on sensor: %s", sensorUuid.toString()));

        SensorStatus sensorStatus = this.co2SensorService.getSensorStatus(sensorUuid);

        logger.info(String.format("Fetched sensor status on sensor: %s", sensorUuid.toString()));
        return sensorStatus;
    }


    @ApiOperation(value = "Fetch Sensor Metrics", tags = APITags.CO2_SENSOR_API_TAG)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = APIErrorMessages.REQUEST_BODY_EMPTY)
    })
    @ResponseStatus(CREATED)
    @GetMapping(path = "/{" + SENSOR_UUID + "}/metrics")
    public MetricsResponseDto getSensorMetrics(@PathVariable(name = SENSOR_UUID) UUID sensorUuid) throws Exception {

        logger.info(String.format("Fetching sensor metrics on sensor: %s", sensorUuid.toString()));

        MetricsResponseDto metricsResponseDto = this.co2SensorService.getSensorMetrics(sensorUuid);

        logger.info(String.format("Fetched sensor metrics on sensor: %s", sensorUuid.toString()));
        return metricsResponseDto;
    }


    @ApiOperation(value = "List Alerts", tags = APITags.CO2_SENSOR_API_TAG)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = APIErrorMessages.REQUEST_BODY_EMPTY)
    })
    @ResponseStatus(CREATED)
    @GetMapping(path = "/{" + SENSOR_UUID + "}/alerts")
    public List<AlertResponseDto> listAlerts(@PathVariable(name = SENSOR_UUID) UUID sensorUuid) throws Exception {

        logger.info(String.format("Listing sensor alerts on sensor: %s", sensorUuid.toString()));

        List<AlertResponseDto> sensorAlertList = this.co2SensorService.getSensorAlerts(sensorUuid);

        logger.info(String.format("Listed sensor alerts on sensor: %s", sensorUuid.toString()));
        return sensorAlertList;
    }

}
