package com.musala.drones.service;

import com.musala.drones.dto.DroneMedicationDto;
import com.musala.drones.dto.Medication;
import com.musala.drones.dto.MedicationDto;

import java.util.List;

public interface DroneMedicationService {

    DroneMedicationDto handleCheckLoad(String serialNumber);

    void handleLoadRequest(String serialNumber, MedicationDto medications);

}
