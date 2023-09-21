package com.musala.drones.service.impl;

import com.musala.drones.dto.DroneBatteryCapacityDto;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.exception.BadRequestException;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.State;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.DroneService;
import com.musala.drones.util.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final DtoMapper dtoMapper;

    public DroneServiceImpl(DroneRepository droneRepository, DtoMapper dtoMapper) {
        this.droneRepository = droneRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public List<DroneDto> handleFindAllDrones() {
        final List<DroneDto> drones = new ArrayList<>();
        droneRepository.findAllByState(State.IDLE).forEach(droneEntity -> drones.add(dtoMapper.droneEntityToDtoDrone(droneEntity)));
        return drones;
    }

    @Override
    public DroneBatteryCapacityDto checkBatteryCapacity(String serialNumber) {
        final DroneEntity drone = findDroneEntity(serialNumber);
        final DroneBatteryCapacityDto response = new DroneBatteryCapacityDto();
        response.setSerialNumber(serialNumber);
        response.setBatteryCapacity(drone.getBatteryCapacity());
        return response;
    }

    private DroneEntity findDroneEntity(String serialNumber) {
        final Optional<DroneEntity> droneEntityOptional = droneRepository.findDroneEntityBySerialNumber(serialNumber);
        if (droneEntityOptional.isPresent()) {
            log.info("Found a drone with serialNumber {}", serialNumber);
            return droneEntityOptional.get();
        } else {
            throw new BadRequestException("Not found drone with serialNumber " + serialNumber);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED, rollbackFor = DataIntegrityViolationException.class)
    @Override
    public void handleRegisterRequest(DroneDto drone) {
        final DroneEntity droneEntity = dtoMapper.dtoDroneToDroneEntity(drone);
        try {
            if (droneRepository.findDroneEntityBySerialNumber(drone.getSerialNumber()).isPresent()) {
                throw new BadRequestException("Drone with " + drone.getSerialNumber() + " already exists");
            }
            final Long id = droneRepository.save(droneEntity).getId();
            log.info("Saved drone with id {}", id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e.getCause().getCause().getMessage());
        }
    }
}