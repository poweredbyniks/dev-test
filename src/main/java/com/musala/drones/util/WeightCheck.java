package com.musala.drones.util;

import com.musala.drones.dto.Medication;
import com.musala.drones.dto.MedicationDto;

public class WeightCheck {

    public static boolean checkWeight(MedicationDto medications) {
        return (medications.getMedications().stream().mapToInt(Medication::getWeight).sum() <= 500);
    }
}