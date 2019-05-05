package info.alaz.sensor.co2.repository;

import info.alaz.sensor.co2.entity.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SensorRepository extends JpaRepository<SensorEntity, UUID> {

}
