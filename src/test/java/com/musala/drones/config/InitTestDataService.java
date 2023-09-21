package com.musala.drones.config;

import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.MedicationEntity;
import com.musala.drones.model.Model;
import com.musala.drones.model.State;
import com.musala.drones.repository.DroneRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class InitTestDataService {

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationEntityRepository medicationEntityRepository;

    @Transactional
    public void initDataForPositiveCaseAvailableDrones() {
        final DroneEntity droneEntity1 = new DroneEntity();
        droneEntity1.setState(State.IDLE);
        droneEntity1.setModel(Model.Heavyweight);
        droneEntity1.setBatteryCapacity(100);
        droneEntity1.setSerialNumber(UUID.randomUUID().toString());
        droneEntity1.setWeightLimit(500);

        final DroneEntity droneEntity2 = new DroneEntity();
        droneEntity2.setState(State.RETURNING);
        droneEntity2.setModel(Model.Heavyweight);
        droneEntity2.setBatteryCapacity(22);
        droneEntity2.setSerialNumber(UUID.randomUUID().toString());
        droneEntity2.setWeightLimit(500);

        final DroneEntity droneEntity3 = new DroneEntity();
        droneEntity3.setState(State.RETURNING);
        droneEntity3.setModel(Model.Heavyweight);
        droneEntity3.setBatteryCapacity(75);
        droneEntity3.setSerialNumber(UUID.randomUUID().toString());
        droneEntity3.setWeightLimit(500);

        droneRepository.save(droneEntity1);
        droneRepository.save(droneEntity2);
        droneRepository.save(droneEntity3);
    }

    @Transactional
    public void initDataForPositiveCaseGetDroneBatteryCapacity() {
        final DroneEntity droneEntity1 = new DroneEntity();
        droneEntity1.setState(State.IDLE);
        droneEntity1.setModel(Model.Heavyweight);
        droneEntity1.setBatteryCapacity(42);
        droneEntity1.setSerialNumber("C-3PO");
        droneEntity1.setWeightLimit(500);

        final DroneEntity droneEntity2 = new DroneEntity();
        droneEntity2.setState(State.RETURNING);
        droneEntity2.setModel(Model.Heavyweight);
        droneEntity2.setBatteryCapacity(22);
        droneEntity2.setSerialNumber(UUID.randomUUID().toString());
        droneEntity2.setWeightLimit(500);

        final DroneEntity droneEntity3 = new DroneEntity();
        droneEntity3.setState(State.RETURNING);
        droneEntity3.setModel(Model.Heavyweight);
        droneEntity3.setBatteryCapacity(75);
        droneEntity3.setSerialNumber(UUID.randomUUID().toString());
        droneEntity3.setWeightLimit(500);

        droneRepository.save(droneEntity1);
        droneRepository.save(droneEntity2);
        droneRepository.save(droneEntity3);
    }

    @Transactional
    public void initDataForNegativePostDroneWithExistingSerialNumber() {
        final DroneEntity droneEntity1 = new DroneEntity();
        droneEntity1.setState(State.IDLE);
        droneEntity1.setModel(Model.Heavyweight);
        droneEntity1.setBatteryCapacity(42);
        droneEntity1.setSerialNumber("C-3PO");
        droneEntity1.setWeightLimit(500);

        final DroneEntity droneEntity2 = new DroneEntity();
        droneEntity2.setState(State.RETURNING);
        droneEntity2.setModel(Model.Heavyweight);
        droneEntity2.setBatteryCapacity(22);
        droneEntity2.setSerialNumber(UUID.randomUUID().toString());
        droneEntity2.setWeightLimit(500);

        final DroneEntity droneEntity3 = new DroneEntity();
        droneEntity3.setState(State.RETURNING);
        droneEntity3.setModel(Model.Heavyweight);
        droneEntity3.setBatteryCapacity(75);
        droneEntity3.setSerialNumber(UUID.randomUUID().toString());
        droneEntity3.setWeightLimit(500);

        droneRepository.save(droneEntity1);
        droneRepository.save(droneEntity2);
        droneRepository.save(droneEntity3);
    }

    @SneakyThrows
    @Transactional
    public void initDataForPositiveCaseCheckDroneMedicines() {
        final DroneEntity droneEntity1 = new DroneEntity();
        droneEntity1.setState(State.IDLE);
        droneEntity1.setModel(Model.Heavyweight);
        droneEntity1.setBatteryCapacity(42);
        droneEntity1.setSerialNumber("C-3PO");
        droneEntity1.setWeightLimit(500);


        final MedicationEntity medication1 = new MedicationEntity();
        medication1.setDrones(droneEntity1);
        medication1.setCode("42");
        medication1.setName("aspirin");
        medication1.setWeight(100);
        final String aspirinImage = new String(Files.readAllBytes(Paths.get("src/test/resources/aspirin-image.txt")));
        medication1.setImage(aspirinImage.getBytes());
        medicationEntityRepository.save(medication1);

        final MedicationEntity medication2 = new MedicationEntity();
        medication2.setDrones(droneEntity1);
        medication2.setCode("1000");
        medication2.setName("antiseptic");
        medication2.setWeight(150);
        final String antisepticImage = new String(Files.readAllBytes(Paths.get("src/test/resources/antiseptic-image.txt")));
        medication2.setImage(antisepticImage.getBytes());
        medicationEntityRepository.save(medication2);

        final DroneEntity droneEntity2 = new DroneEntity();
        droneEntity2.setState(State.RETURNING);
        droneEntity2.setModel(Model.Heavyweight);
        droneEntity2.setBatteryCapacity(22);
        droneEntity2.setSerialNumber(UUID.randomUUID().toString());
        droneEntity2.setWeightLimit(500);

        final DroneEntity droneEntity3 = new DroneEntity();
        droneEntity3.setState(State.RETURNING);
        droneEntity3.setModel(Model.Heavyweight);
        droneEntity3.setBatteryCapacity(75);
        droneEntity3.setSerialNumber(UUID.randomUUID().toString());
        droneEntity3.setWeightLimit(500);

        droneRepository.save(droneEntity1);
        droneRepository.save(droneEntity2);
        droneRepository.save(droneEntity3);
    }

    @SneakyThrows
    @Transactional
    public void initDataForNegativeWrongState() {
        final DroneEntity droneEntity1 = new DroneEntity();
        droneEntity1.setState(State.RETURNING);
        droneEntity1.setModel(Model.Heavyweight);
        droneEntity1.setBatteryCapacity(42);
        droneEntity1.setSerialNumber("C-3PO");
        droneEntity1.setWeightLimit(500);


        final DroneEntity droneEntity2 = new DroneEntity();
        droneEntity2.setState(State.RETURNING);
        droneEntity2.setModel(Model.Heavyweight);
        droneEntity2.setBatteryCapacity(22);
        droneEntity2.setSerialNumber(UUID.randomUUID().toString());
        droneEntity2.setWeightLimit(500);

        final DroneEntity droneEntity3 = new DroneEntity();
        droneEntity3.setState(State.RETURNING);
        droneEntity3.setModel(Model.Heavyweight);
        droneEntity3.setBatteryCapacity(75);
        droneEntity3.setSerialNumber(UUID.randomUUID().toString());
        droneEntity3.setWeightLimit(500);

        droneRepository.save(droneEntity1);
        droneRepository.save(droneEntity2);
        droneRepository.save(droneEntity3);
    }

}
