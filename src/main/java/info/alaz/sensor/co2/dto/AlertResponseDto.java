package info.alaz.sensor.co2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AlertResponseDto", description = "AlertEntity Response DTO")
public class AlertResponseDto {

    @NotNull
    @ApiModelProperty(notes = "Log message", example = "2019-02-02T18:55:47+0000", position = 1)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime startTime;

    @NotNull
    @ApiModelProperty(notes = "Log's creation time.", example = "2019-02-02T20:00:47+0000", position = 2)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime endTime;

    @ApiModelProperty(notes = "First measurement", example = "2100", position = 3)
    private Integer measurement1;

    @ApiModelProperty(notes = "Second measurement.", example = "2200", position = 3)
    private Integer measurement2;

    @ApiModelProperty(notes = "Third measurement", example = "2100", position = 3)
    private Integer measurement3;

}
