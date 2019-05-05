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
@Table(name = "tbl_measurement")
public class MeasurementEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fld_sensor_id", nullable = false)
    private SensorEntity sensor;

    @NotNull
    @Column(name = "fld_co2_level")
    private Integer co2Level;

    @NotNull
    @Column(name = "fld_date_measured")
    private ZonedDateTime dateMeasured;
}
