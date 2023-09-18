package com.musala.drones.service.impl;

import com.musala.drones.dto.DroneMedicationDto;
import com.musala.drones.dto.Medication;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.exception.InternalServerErrorException;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.MedicationEntity;
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

    public DroneMedicationServiceImpl(DroneRepository droneRepository, DtoMapper dtoMapper) {
        this.droneRepository = droneRepository;
        this.dtoMapper = dtoMapper;
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
        if (WeightCheck.checkWeight(medications)) {
            handlePositive(serialNumber, medications);
        } else {
            throw new InternalServerErrorException("Total wight of medications must not exceed 500gr");
        }
    }

    private void handlePositive(String serialNumber, MedicationDto medications) {
        final List<MedicationEntity> medicationEntityList = new ArrayList<>();
        medications.getMedications().forEach(medication -> medicationEntityList.add(dtoMapper.medicationDtoToMedicationEntity(medication)));
        final DroneEntity droneEntity = findDroneEntity(serialNumber);
        if (droneEntity.getBatteryCapacity() >= 25) {
            updateEntity(droneEntity, medicationEntityList);
        } else {
            throw new InternalServerErrorException("Battery capacity must be 25 % or more");
        }
    }

    private DroneEntity findDroneEntity(String serialNumber) {
        final Optional<DroneEntity> droneEntityOptional = droneRepository.findDroneEntityBySerialNumber(serialNumber);
        if (droneEntityOptional.isPresent()) {
            return droneEntityOptional.get();
        } else {
            throw new InternalServerErrorException("Not found drone with serialNumber " + serialNumber);
        }
    }

    private void updateEntity(DroneEntity droneEntity, List<MedicationEntity> medicationEntityList) {
        droneEntity.getMedicationEntities().addAll(medicationEntityList);
        final Long id = droneRepository.save(droneEntity).getId();
        log.info("Updated droneEntity with id {}", id);
    }

}
