package com.musala.drones.service.impl;

import com.musala.drones.dto.DroneMedicationDto;
import com.musala.drones.dto.Medication;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.exception.BadRequestException;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.MedicationEntity;
import com.musala.drones.model.State;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.DroneMedicationService;
import com.musala.drones.util.DtoMapper;
import com.musala.drones.util.WeightCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class DroneMedicationServiceImpl implements DroneMedicationService {

    private final DroneRepository droneRepository;

    private final DtoMapper dtoMapper;

    private final WeightCheck weightCheckUtil;

    public DroneMedicationServiceImpl(DroneRepository droneRepository, DtoMapper dtoMapper, WeightCheck weightCheckUtil) {
        this.droneRepository = droneRepository;
        this.dtoMapper = dtoMapper;
        this.weightCheckUtil = weightCheckUtil;
    }

    @Override
    public DroneMedicationDto handleCheckLoad(String serialNumber) {
        final DroneEntity droneEntity = findDroneEntity(serialNumber);
        final Set<MedicationEntity> medicationEntities = droneEntity.getMedicationEntities();
        final List<Medication> medicationDtos = new ArrayList<>();
        medicationEntities.forEach(medication -> medicationDtos.add(dtoMapper.medicationEntityToMedicationDto(medication)));
        final DroneMedicationDto response = new DroneMedicationDto();
        response.setSerialNumber(serialNumber);
        response.setMedications(medicationDtos);
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public void handleLoadRequest(String serialNumber, MedicationDto medications) {
        if (weightCheckUtil.checkWeight(medications)) {
            handlePositive(serialNumber, medications);
        } else {
            throw new BadRequestException("Total weight of medications must not exceed 500gr");
        }
    }

    private void handlePositive(String serialNumber, MedicationDto medications) {
        final List<MedicationEntity> medicationEntityList = new ArrayList<>();
        final DroneEntity droneEntity = findDroneEntityToLoad(serialNumber);
        if (droneEntity.getBatteryCapacity() >= 25) {
            medications.getMedications().forEach(medication -> medicationEntityList.add(dtoMapper.medicationDtoToMedicationEntity(medication)));
            updateEntity(droneEntity, medicationEntityList);
        } else {
            throw new BadRequestException("Battery capacity must be 25 % or more");
        }
    }

    private DroneEntity findDroneEntity(String serialNumber) {
        final Optional<DroneEntity> droneEntityOptional = droneRepository.findDroneEntityBySerialNumber(serialNumber);
        if (droneEntityOptional.isPresent()) {
            return droneEntityOptional.get();
        } else {
            throw new BadRequestException("Not found drone with serialNumber " + serialNumber);
        }
    }

    private DroneEntity findDroneEntityToLoad(String serialNumber) {
        final Optional<DroneEntity> droneEntityOptional =
                droneRepository.findDroneEntityBySerialNumberAndStateIdleOrLoading(serialNumber, State.IDLE, State.LOADING);
        if (droneEntityOptional.isPresent()) {
            return droneEntityOptional.get();
        } else {
            throw new BadRequestException("Not found drone with serialNumber " + serialNumber + " and IDLE or LOADING states");
        }
    }

    private void updateEntity(DroneEntity droneEntity, List<MedicationEntity> medicationEntityList) {
        medicationEntityList.forEach(medicationEntity -> medicationEntity.setDrones(droneEntity));
        droneEntity.getMedicationEntities().addAll(medicationEntityList);
        droneEntity.setState(State.LOADING);
        if (weightCheckUtil.checkWeight(droneEntity.getMedicationEntities())) {
            final Long id = droneRepository.save(droneEntity).getId();
            log.info("Updated droneEntity with id {}", id);
        } else {
            throw new BadRequestException("Total weight of medications must not exceed 500gr");
        }
    }
}