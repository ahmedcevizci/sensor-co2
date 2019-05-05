package info.alaz.sensor.co2.mapper;


import info.alaz.sensor.co2.dto.AlertResponseDto;
import info.alaz.sensor.co2.entity.AlertEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlertMapper extends GenericEntityMapper<AlertResponseDto, AlertEntity> {

    @Mapping(source = "dateStarted", target = "startTime")
    @Mapping(source = "dateEnded", target = "endTime")
    @Mapping(source = "firstCo2Level", target = "measurement1")
    @Mapping(source = "secondCo2Level", target = "measurement2")
    @Mapping(source = "thirdCo2Level", target = "measurement3")
    AlertResponseDto toDto(AlertEntity entity);
}