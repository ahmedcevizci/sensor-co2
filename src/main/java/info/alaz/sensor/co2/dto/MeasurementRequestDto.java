package info.alaz.sensor.co2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "MeasurementRequestDto", description = "MeasurementEntity Request DTO")
public class MeasurementRequestDto {

    @NotNull
    @Min(0)
    @ApiModelProperty(notes = "CO2 levels", example = "2000", position = 1)
    private Integer co2;

    @NotNull
    @ApiModelProperty(notes = "MeasurementEntity timestamp", example = "2019-02-01T18:55:47+0000", position = 2)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime time;
}
