package info.alaz.sensor.co2.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "sensor.co2")
@AllArgsConstructor
@NoArgsConstructor
public class Co2SensorProperties {

    @NotNull
    private Integer alertingCo2Levels;

}
