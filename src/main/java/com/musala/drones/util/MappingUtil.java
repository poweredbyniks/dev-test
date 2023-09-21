package com.musala.drones.util;

import com.musala.drones.dto.DroneBatteryCapacityDto;
import com.musala.drones.dto.DroneMedicationDto;
import com.musala.drones.dto.Medication;
import com.musala.drones.model.DroneEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MappingUtil {

    public DroneBatteryCapacityDto buildDroneBatteryCapacityDto(String serialNumber, DroneEntity drone) {
        final DroneBatteryCapacityDto response = new DroneBatteryCapacityDto();
        response.setSerialNumber(serialNumber);
        response.setBatteryCapacity(drone.getBatteryCapacity());
        return response;
    }

    public DroneMedicationDto buildDroneMedicationDto(String serialNumber, List<Medication> medicationDtos) {
        final DroneMedicationDto response = new DroneMedicationDto();
        response.setSerialNumber(serialNumber);
        response.setMedications(medicationDtos);
        return response;
    }

}
