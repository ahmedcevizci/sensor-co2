package info.alaz.sensor.co2.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SensorEntity Status", description = "SensorEntity Status Enum")
public enum SensorStatus {

    @ApiModelProperty(notes = "CO2 levels are in normal levels.", example = "OK")
    OK,
    @ApiModelProperty(notes = "CO2 levels are increasing into worrying levels.", example = "WARN")
    WARN,
    @ApiModelProperty(notes = "CO2 levels are in dangerous levels.", example = "ALERT")
    ALERT
}
