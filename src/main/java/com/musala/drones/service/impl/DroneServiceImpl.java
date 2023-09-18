package com.musala.drones.service.impl;

import com.musala.drones.dto.CommonErrorDto;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.exception.InternalServerErrorException;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.MedicationEntity;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.DroneService;
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

@Slf4j
@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final DtoMapper dtoMapper;

    public DroneServiceImpl(DroneRepository droneRepository, DtoMapper dtoMapper) {
        this.droneRepository = droneRepository;
        this.dtoMapper = dtoMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public void handleRegisterRequest(DroneDto drone) {
        final DroneEntity droneEntity = dtoMapper.dtoDroneToDroneEntity(drone);
        final Long id = droneRepository.save(droneEntity).getId();
        log.info("Saved drone with id {}", id);
    }



}