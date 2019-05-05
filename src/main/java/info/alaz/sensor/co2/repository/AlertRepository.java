package info.alaz.sensor.co2.repository;

import info.alaz.sensor.co2.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<AlertEntity, UUID> {

    List<AlertEntity> findAllBySensor_UuidOrderByDateEndedDesc(UUID sensorUuid);
}
