package com.musala.drones.service.impl;

import com.musala.drones.model.DroneEntity;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.BatteryLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BatteryLogServiceImpl implements BatteryLogService {

    private final DroneRepository droneRepository;

    public BatteryLogServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public void checkAndLogBatteryCapacity() {
        final List<DroneEntity> entityList = droneRepository.findAllEntities();
        log.info("Logged drones battery status");
        entityList.forEach(entity -> MDC.put(entity.getSerialNumber(), String.valueOf(entity.getBatteryCapacity())));
    }
}