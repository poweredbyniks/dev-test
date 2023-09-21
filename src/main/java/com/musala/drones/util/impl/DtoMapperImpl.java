package com.musala.drones.util.impl;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.Medication;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.MedicationEntity;
import com.musala.drones.util.DtoMapper;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-09-19T14:47:45+0400",
        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.5 (Homebrew)"
)
@Component
public class DtoMapperImpl implements DtoMapper {

    @Override
    public DroneDto droneEntityToDtoDrone(DroneEntity droneEntity) {
        if (droneEntity == null) {
            return null;
        }

        DroneDto droneDto = new DroneDto();

        droneDto.setSerialNumber(droneEntity.getSerialNumber());
        droneDto.setModel(droneEntity.getModel());
        if (droneEntity.getWeightLimit() != null) {
            droneDto.setWeightLimit(droneEntity.getWeightLimit());
        }
        droneDto.setBatteryCapacity(droneEntity.getBatteryCapacity());
        droneDto.setState(droneEntity.getState());

        return droneDto;
    }

    @Override
    public DroneEntity dtoDroneToDroneEntity(DroneDto drone) {
        if (drone == null) {
            return null;
        }

        DroneEntity.DroneEntityBuilder droneEntity = DroneEntity.builder();

        droneEntity.serialNumber(drone.getSerialNumber());
        droneEntity.model(drone.getModel());
        if (drone.getWeightLimit() != null) {
            droneEntity.weightLimit(drone.getWeightLimit());
        }
        droneEntity.batteryCapacity(drone.getBatteryCapacity());
        droneEntity.state(drone.getState());

        return droneEntity.build();
    }

    @Override
    public Medication medicationEntityToMedicationDto(MedicationEntity medication) {
        if (medication == null) {
            return null;
        }

        Medication medication1 = new Medication();

        medication1.setName(medication.getName());
        medication1.setWeight(medication.getWeight());
        medication1.setCode(medication.getCode());
        byte[] image = medication.getImage();
        if (image != null) {
            medication1.setImage(new String(Arrays.copyOf(image, image.length)));
        }

        return medication1;
    }

    @Override
    public MedicationEntity medicationDtoToMedicationEntity(Medication medication) {
        if (medication == null) {
            return null;
        }

        MedicationEntity.MedicationEntityBuilder medicationEntity = MedicationEntity.builder();

        medicationEntity.name(medication.getName());
        medicationEntity.weight(medication.getWeight());
        medicationEntity.code(medication.getCode());

        byte[] image = medication.getImage().getBytes(StandardCharsets.UTF_8);
        if (image != null) {
            medicationEntity.image(Arrays.copyOf(image, image.length));
        }

        return medicationEntity.build();
    }
}
