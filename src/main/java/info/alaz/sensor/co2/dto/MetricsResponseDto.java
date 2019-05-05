package info.alaz.sensor.co2.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "MetricsResponseDto", description = "Metrics Response DTO")
public class MetricsResponseDto {

    @NotNull
    @Min(0)
    @ApiModelProperty(notes = "CO2 levels", example = "1200", position = 1)
    private Integer maxLast30Days;

    @NotNull
    @Min(0)
    @ApiModelProperty(notes = "CO2 levels", example = "900", position = 2)
    private Integer avgLast30Days;

}
