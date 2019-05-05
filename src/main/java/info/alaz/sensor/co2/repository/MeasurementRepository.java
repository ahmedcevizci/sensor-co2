package info.alaz.sensor.co2.repository;

import info.alaz.sensor.co2.entity.MeasurementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<MeasurementEntity, UUID> {

    Page<MeasurementEntity> findAllBySensor_UuidOrderByDateMeasured(UUID sensorUuid, Pageable pageable);

    @Query(value = "SELECT MAX(co2Level) FROM MeasurementEntity m WHERE m.sensor.uuid = ?1 AND m.dateMeasured > ?2")
    Integer getMaxCo2LevelOfSensorAfterGivenDate(UUID sensorUuid, ZonedDateTime afterDate);

    @Query(value = "SELECT AVG(co2Level) FROM MeasurementEntity m WHERE m.sensor.uuid = ?1 AND m.dateMeasured > ?2")
    Integer getAverageCo2LevelOfSensorAfterGivenDate(UUID sensorUuid, ZonedDateTime afterDate);
}
