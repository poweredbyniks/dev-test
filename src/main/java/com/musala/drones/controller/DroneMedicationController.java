package com.musala.drones.controller;

import com.musala.drones.dto.DroneMedicationDto;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.service.DroneMedicationService;
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

@Slf4j
@RequestMapping("${service.mapping.common}")
@RestController
public class DroneMedicationController {

    private final DroneMedicationService droneMedicationService;

    public DroneMedicationController(DroneMedicationService droneMedicationService) {
        this.droneMedicationService = droneMedicationService;
    }

    @GetMapping("${service.mapping.load-drone-post}")
    public ResponseEntity<DroneMedicationDto> checkLoad(@PathVariable String serialNumber) {
        final DroneMedicationDto droneMedicationDto = droneMedicationService.handleCheckLoad(serialNumber);
        return new ResponseEntity<>(droneMedicationDto, HttpStatus.OK);
    }

    @PostMapping("${service.mapping.load-drone-post}")
    public ResponseEntity<String> loadDrone(@RequestHeader String processId,
                                            @PathVariable String serialNumber,
                                            @RequestBody MedicationDto medications) {
        droneMedicationService.handleLoadRequest(serialNumber, medications);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
