package info.alaz.sensor.co2.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_alert")
public class AlertEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fld_sensor_id", nullable = false)
    private SensorEntity sensor;

    @NotNull
    @Column(name = "fld_first_co2_level")
    private Integer firstCo2Level;

    @NotNull
    @Column(name = "fld_second_co2_level")
    private Integer secondCo2Level;

    @NotNull
    @Column(name = "fld_third_co2_level")
    private Integer thirdCo2Level;

    @NotNull
    @Column(name = "fld_date_started")
    private ZonedDateTime dateStarted;

    @NotNull
    @Column(name = "fld_date_ended")
    private ZonedDateTime dateEnded;
}
