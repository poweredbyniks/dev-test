package com.musala.drones.controller;

import com.musala.drones.dto.DroneBatteryCapacityDto;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("${service.mapping.common}")
@RestController
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("${service.mapping.get-all-available-drones}")
    public ResponseEntity<List<DroneDto>> findAllAvailableDrones() {
        final List<DroneDto> drones = droneService.handleFindAllDrones();
        return new ResponseEntity<>(drones, HttpStatus.OK);
    }

    @GetMapping("${service.mapping.get-drone-battery-capacity}")
    public ResponseEntity<DroneBatteryCapacityDto> checkBatteryCapacity(@PathVariable String serialNumber) {
        final DroneBatteryCapacityDto batteryCapacity = droneService.checkBatteryCapacity(serialNumber);
        return new ResponseEntity<>(batteryCapacity, HttpStatus.OK);
    }

    @PostMapping("${service.mapping.register-drone-post}")
    public ResponseEntity<String> registerDrone(@RequestHeader String processId, @Valid @RequestBody DroneDto drone) {
        log.info("Received POST request /drones");
        droneService.handleRegisterRequest(drone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}