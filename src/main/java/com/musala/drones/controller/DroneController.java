package com.musala.drones.controller;

import com.musala.drones.dto.DroneBatteryCapacityDto;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.service.DroneService;
import com.musala.drones.util.ProcessIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
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

import static com.musala.drones.util.ProcessIdUtil.PROCESS_ID_HEADER_NAME;

@Slf4j
@RequestMapping("${service.mapping.common}")
@RestController
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("${service.mapping.get-all-available-drones}")
    public ResponseEntity<List<DroneDto>> findAllAvailableDrones(@RequestHeader(required = false) String processId) {
        MDC.put(PROCESS_ID_HEADER_NAME, ProcessIdUtil.checkAndGenerateProcessId(processId));
        log.info("Received GET request /v1/available-drones request");
        final List<DroneDto> drones = droneService.handleFindAllDrones();
        return new ResponseEntity<>(drones, HttpStatus.OK);
    }

    @GetMapping("${service.mapping.get-drone-battery-capacity}")
    public ResponseEntity<DroneBatteryCapacityDto> checkBatteryCapacity(@RequestHeader(required = false) String processId,
                                                                        @PathVariable String serialNumber) {
        MDC.put(PROCESS_ID_HEADER_NAME, ProcessIdUtil.checkAndGenerateProcessId(processId));
        log.info("Received GET request /v1/drones/{serialNumber}/capacity request");
        final DroneBatteryCapacityDto batteryCapacity = droneService.checkBatteryCapacity(serialNumber);
        return new ResponseEntity<>(batteryCapacity, HttpStatus.OK);
    }

    @PostMapping("${service.mapping.register-drone-post}")
    public ResponseEntity<Void> registerDrone(@RequestHeader(required = false) String processId, @Valid @RequestBody DroneDto drone) {
        MDC.put(PROCESS_ID_HEADER_NAME, ProcessIdUtil.checkAndGenerateProcessId(processId));
        log.info("Received POST request /v1/drones request");
        droneService.handleRegisterRequest(drone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}