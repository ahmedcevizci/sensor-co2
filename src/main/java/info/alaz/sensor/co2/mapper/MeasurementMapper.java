package info.alaz.sensor.co2.mapper;


import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.entity.MeasurementEntity;
import info.alaz.sensor.co2.entity.SensorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeasurementMapper extends GenericEntityMapper<MeasurementRequestDto, MeasurementEntity> {

    @Mapping(source = "uuid", target = "uuid")
    @Mapping(source = "sensorEntity", target = "sensor")
    @Mapping(source = "dto.co2", target = "co2Level")
    @Mapping(source = "dto.time", target = "dateMeasured")
    MeasurementEntity toEntity(UUID uuid, MeasurementRequestDto dto, SensorEntity sensorEntity);

}