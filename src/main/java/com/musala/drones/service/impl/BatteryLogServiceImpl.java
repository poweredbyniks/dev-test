package com.musala.drones.service.impl;

import com.musala.drones.model.BatteryLog;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.repository.BatteryLogRepository;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.BatteryLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BatteryLogServiceImpl implements BatteryLogService {

    private final DroneRepository droneRepository;

    private final BatteryLogRepository batteryLogRepository;

    public BatteryLogServiceImpl(DroneRepository droneRepository, BatteryLogRepository batteryLogRepository) {
        this.droneRepository = droneRepository;
        this.batteryLogRepository = batteryLogRepository;
    }

    @Override
    public void checkAndLogBatteryCapacity() {
        final List<DroneEntity> entityList = droneRepository.findAllEntities();
        final List<BatteryLog> batteryLogs = new ArrayList<>();
        entityList.forEach(entity -> {
            batteryLogs.add(buildBatteryLog(entity));
        });
        batteryLogRepository.saveAll(batteryLogs);
        log.info("Successfully logged drones battery status");
    }

    private BatteryLog buildBatteryLog(DroneEntity entity) {
        final BatteryLog batteryLog = new BatteryLog();
        batteryLog.setLogDateTime(ZonedDateTime.now());
        batteryLog.setSerialNumber(entity.getSerialNumber());
        batteryLog.setBatteryCapacity(entity.getBatteryCapacity());
        return batteryLog;
    }
}