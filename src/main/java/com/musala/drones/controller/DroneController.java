package com.musala.drones.controller;

import com.musala.drones.dto.DroneDto;
import com.musala.drones.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("${service.mapping.common}")
@RestController
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("${service.mapping.register-drone-post}")
    public ResponseEntity<String> registerDrone(@RequestHeader String processId, @Valid @RequestBody DroneDto drone) {
        log.info("Received POST request /drones");
        droneService.handleRegisterRequest(drone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}