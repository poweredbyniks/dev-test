package com.musala.drones.config;

import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.Model;
import com.musala.drones.model.State;
import com.musala.drones.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class InitTestDataService {

    @Autowired
    private DroneRepository droneRepository;

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

}
