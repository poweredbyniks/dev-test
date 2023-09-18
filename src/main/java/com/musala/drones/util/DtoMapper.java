package com.musala.drones.util;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.dto.Medication;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.MedicationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    DroneDto droneEntityToDtoDrone(DroneEntity droneEntity);

    DroneEntity dtoDroneToDroneEntity(DroneDto drone);

    Medication medicationEntityToMedicationDto(MedicationEntity medication);

    MedicationEntity medicationDtoToMedicationEntity(Medication medication);

}
