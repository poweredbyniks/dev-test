package com.musala.drones.util;

import com.musala.drones.dto.Medication;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.model.MedicationEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class WeightCheck {

    public boolean checkWeight(MedicationDto medications) {
        return (medications.getMedications().stream().mapToInt(Medication::getWeight).sum() <= 500);
    }

    public boolean checkWeight(Set<MedicationEntity> medications) {
        return (medications.stream().mapToInt(MedicationEntity::getWeight).sum() <= 500);
    }
}