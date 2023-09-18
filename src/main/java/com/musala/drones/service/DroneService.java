package com.musala.drones.service;

import com.musala.drones.dto.DroneBatteryCapacityDto;
import com.musala.drones.dto.DroneDto;

import java.util.List;

public interface DroneService {

    List<DroneDto> handleFindAllDrones();

    DroneBatteryCapacityDto checkBatteryCapacity(String serialNumber);


    void handleRegisterRequest(DroneDto drone);


}
