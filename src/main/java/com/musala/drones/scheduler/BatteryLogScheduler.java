package com.musala.drones.scheduler;

import com.musala.drones.service.BatteryLogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatteryLogScheduler {

    private final BatteryLogService batteryLogService;

    public BatteryLogScheduler(BatteryLogService batteryLogService) {
        this.batteryLogService = batteryLogService;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void runTask() {
        batteryLogService.checkAndLogBatteryCapacity();
    }
}
