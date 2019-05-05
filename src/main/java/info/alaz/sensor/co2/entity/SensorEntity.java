package info.alaz.sensor.co2.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_sensor")
public class SensorEntity extends BaseEntity {

    @NotNull
    @Column(name = "fld_status")
    @Enumerated(EnumType.STRING)
    private SensorStatus status;

    @Column(name = "fld_date_status_changed")
    private ZonedDateTime dateStatusChanged;

    @NotNull
    @Column(name = "fld_date_created")
    private ZonedDateTime dateCreated;

    @OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "dateEnded DESC")
    private Set<AlertEntity> alerts;

}
