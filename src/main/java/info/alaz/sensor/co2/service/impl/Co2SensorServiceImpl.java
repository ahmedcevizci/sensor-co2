package info.alaz.sensor.co2.service.impl;

import info.alaz.sensor.co2.config.Co2SensorProperties;
import info.alaz.sensor.co2.dto.AlertResponseDto;
import info.alaz.sensor.co2.dto.MeasurementRequestDto;
import info.alaz.sensor.co2.dto.MetricsResponseDto;
import info.alaz.sensor.co2.entity.SensorStatus;
import info.alaz.sensor.co2.entity.AlertEntity;
import info.alaz.sensor.co2.entity.MeasurementEntity;
import info.alaz.sensor.co2.entity.SensorEntity;
import info.alaz.sensor.co2.exception.SensorNotFoundException;
import info.alaz.sensor.co2.mapper.AlertMapper;
import info.alaz.sensor.co2.mapper.MeasurementMapper;
import info.alaz.sensor.co2.repository.AlertRepository;
import info.alaz.sensor.co2.repository.MeasurementRepository;
import info.alaz.sensor.co2.repository.SensorRepository;
import info.alaz.sensor.co2.service.Co2SensorService;
import info.alaz.sensor.co2.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class Co2SensorServiceImpl implements Co2SensorService {

    private Logger logger = LoggerFactory.getLogger(Co2SensorServiceImpl.class);

    private SensorRepository sensorRepository;
    private MeasurementRepository measurementRepository;
    private AlertRepository alertRepository;
    private MeasurementMapper measurementMapper;
    private AlertMapper alertMapper;

    private Integer alertingCo2Levels;

    @Autowired
    public Co2SensorServiceImpl(SensorRepository sensorRepository, MeasurementRepository measurementRepository, AlertRepository alertRepository, MeasurementMapper measurementMapper, AlertMapper alertMapper, Co2SensorProperties co2SensorProperties) {
        this.sensorRepository = sensorRepository;
        this.measurementRepository = measurementRepository;
        this.alertRepository = alertRepository;
        this.measurementMapper = measurementMapper;
        this.alertMapper = alertMapper;
        this.alertingCo2Levels = co2SensorProperties.getAlertingCo2Levels();
    }

    @Override
    @Transactional
    public void saveMeasurement(UUID sensorUuid, MeasurementRequestDto measurementRequestDto) throws SensorNotFoundException {
        logger.debug("Saving new measurement");

        SensorEntity sensorEntity = getSensorEntity(sensorUuid);
        //TODO check mapping
        MeasurementEntity measurementEntity = this.measurementMapper.toEntity(null, measurementRequestDto, sensorEntity);

        if (measurementEntity.getCo2Level() > this.alertingCo2Levels) {
            handleAlertingMeasurement(sensorUuid, sensorEntity, measurementEntity);
        } else {
            handleNormalMeasurement(sensorUuid, sensorEntity, measurementEntity);
        }

        this.measurementRepository.save(measurementEntity);

        logger.debug("Saved new measurement");
    }

    private void handleNormalMeasurement(UUID sensorUuid, SensorEntity sensorEntity, MeasurementEntity currentMeasurement) {
        SensorStatus previousSensorStatus = sensorEntity.getStatus();
        if (SensorStatus.OK.equals(previousSensorStatus)) {
            // do nothing
            return;
        } else if (SensorStatus.WARN.equals(previousSensorStatus)) {
            this.updateSensorStatus(sensorEntity, SensorStatus.OK);
        } else if (SensorStatus.ALERT.equals(previousSensorStatus)) {
            List<MeasurementEntity> lastMeasurements = this.measurementRepository.findAllBySensor_UuidOrderByDateMeasured(sensorUuid, new PageRequest(0, 2, Sort.Direction.DESC)).getContent();
            this.handleInitialLastMeasurementsValidityInDb(previousSensorStatus, lastMeasurements);
            //Check if it is the third time normal values recorded.
            boolean isAllNormalMeasurements = this.isAllNormalMeasurements(lastMeasurements);

            if (isAllNormalMeasurements) {
                this.updateSensorStatus(sensorEntity, SensorStatus.OK);
            }
        } else {
            throw new IllegalStateException("Apparently new SensorStatus has been defined, please update this switch statement.");
        }

    }

    private void handleAlertingMeasurement(UUID sensorUuid, SensorEntity sensorEntity, MeasurementEntity currentMeasurement) {
        SensorStatus previousSensorStatus = sensorEntity.getStatus();

        if (SensorStatus.OK.equals(previousSensorStatus)) {
            this.updateSensorStatus(sensorEntity, SensorStatus.WARN);
        } else {

            List<MeasurementEntity> lastTwoMeasurements = this.measurementRepository.findAllBySensor_UuidOrderByDateMeasured(sensorUuid, new PageRequest(0, 2, Sort.Direction.DESC)).getContent();
            this.handleInitialLastMeasurementsValidityInDb(previousSensorStatus, lastTwoMeasurements);

            if (SensorStatus.WARN.equals(previousSensorStatus)) {
                if (lastTwoMeasurements.size() == 2) {
                    //Check if it is the third time alerting values recorded.
                    boolean isAllAlertMeasurements = this.isAllAlertMeasurements(lastTwoMeasurements);

                    if (isAllAlertMeasurements) {
                        this.updateSensorStatus(sensorEntity, SensorStatus.ALERT);
                        this.saveNewAlert(sensorEntity, currentMeasurement, lastTwoMeasurements);
                    }
                }
            } else if (SensorStatus.ALERT.equals(previousSensorStatus)) {
                this.saveNewAlert(sensorEntity, currentMeasurement, lastTwoMeasurements);
            } else {
                throw new IllegalStateException("Apparently new SensorStatus has been defined, please update this switch statement.");
            }
        }
    }

    private void handleInitialLastMeasurementsValidityInDb(SensorStatus previousSensorStatus, List<MeasurementEntity> lastMeasurements) {
        if (lastMeasurements.isEmpty()) {
            //There are no previous measurements and state is ALERT or WARNING
            throw new IllegalStateException("Illegal state in database, there should be previous measurements in database for ALERT or WARNING statuses.");
        }
        if (lastMeasurements.size() == 1 && SensorStatus.ALERT.equals(previousSensorStatus)) {
            throw new IllegalStateException("Illegal state in database, there should be at least 2 measurements in database for ALERT status.");
        }
    }

    private void saveNewAlert(SensorEntity sensorEntity, MeasurementEntity thirdMeasurement, List<MeasurementEntity> measurementEntityList) {
        if (measurementEntityList.size() != 2) {
            throw new IllegalStateException("There should be at least 2 measurements in database for ALERT status.");
        }
        MeasurementEntity secondMeasurement = measurementEntityList.get(0);
        MeasurementEntity firstMeasurement = measurementEntityList.get(1);

        this.alertRepository.save(
                new AlertEntity(sensorEntity, firstMeasurement.getCo2Level(), secondMeasurement.getCo2Level(), thirdMeasurement.getCo2Level(),
                        firstMeasurement.getDateMeasured(), thirdMeasurement.getDateMeasured()));
    }

    private boolean isAllAlertMeasurements(List<MeasurementEntity> measurementEntityList) {
        return !(measurementEntityList.stream().anyMatch(measurementEntity -> measurementEntity.getCo2Level() < this.alertingCo2Levels));
    }

    private boolean isAllNormalMeasurements(List<MeasurementEntity> measurementEntityList) {
        return !(measurementEntityList.stream().anyMatch(measurementEntity -> measurementEntity.getCo2Level() > this.alertingCo2Levels));
    }

    private void updateSensorStatus(SensorEntity sensorEntity, SensorStatus newSensorStatus) {
        sensorEntity.setStatus(newSensorStatus);
        sensorEntity.setDateStatusChanged(ZonedDateTime.now());
        this.sensorRepository.save(sensorEntity);
    }

    private SensorEntity getSensorEntity(UUID sensorUuid) throws SensorNotFoundException {
        SensorEntity sensorEntity = this.sensorRepository.findOne(sensorUuid);
        if (sensorEntity == null) {
            throw new SensorNotFoundException(sensorUuid);
        }
        return sensorEntity;
    }

    @Override
    public SensorStatus getSensorStatus(UUID sensorUuid) throws SensorNotFoundException {
        logger.debug("Fetching sensor status");

        SensorEntity sensorEntity = getSensorEntity(sensorUuid);

        logger.debug("Fetched sensor status");
        return sensorEntity.getStatus();
    }

    @Override
    public MetricsResponseDto getSensorMetrics(UUID sensorUuid) throws SensorNotFoundException {
        logger.debug("Fetching sensor metrics");

        SensorEntity sensorEntity = getSensorEntity(sensorUuid);

        ZonedDateTime midnightOfThirtyDaysAgo = DateUtil.getMidnightOfDaysAgo(30);
        Integer maxLast30Days = this.measurementRepository.getMaxCo2LevelOfSensorAfterGivenDate(sensorUuid, midnightOfThirtyDaysAgo);
        Integer avgLast30Days = this.measurementRepository.getAverageCo2LevelOfSensorAfterGivenDate(sensorUuid, midnightOfThirtyDaysAgo);
        MetricsResponseDto metricsResponseDto = new MetricsResponseDto(maxLast30Days, avgLast30Days);
        logger.debug("Fetched sensor metrics");
        return metricsResponseDto;
    }

    @Override
    public List<AlertResponseDto> getSensorAlerts(UUID sensorUuid) throws SensorNotFoundException {
        logger.debug("Fetching sensor alerts");

        SensorEntity sensorEntity = getSensorEntity(sensorUuid);
        List<AlertEntity> alertEntityList = this.alertRepository.findAllBySensor_UuidOrderByDateEndedDesc(sensorUuid);
        //TODO check mapping
        List<AlertResponseDto> alertResponseDtoList = alertMapper.toDtoList(alertEntityList);

        logger.debug("Fetched sensor alerts");
        return alertResponseDtoList;
    }
}
