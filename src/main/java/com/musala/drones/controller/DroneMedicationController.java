package com.musala.drones.controller;

import com.musala.drones.dto.DroneMedicationDto;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.service.DroneMedicationService;
import com.musala.drones.util.ProcessIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.musala.drones.util.ProcessIdUtil.PROCESS_ID_HEADER_NAME;

@Slf4j
@RequestMapping("${service.mapping.common}")
@RestController
public class DroneMedicationController {

    private final DroneMedicationService droneMedicationService;

    public DroneMedicationController(DroneMedicationService droneMedicationService) {
        this.droneMedicationService = droneMedicationService;
    }

    @GetMapping("${service.mapping.check-drone-load-get}")
    public ResponseEntity<DroneMedicationDto> checkLoad(@RequestHeader(required = false) String processId,
                                                        @PathVariable String serialNumber) {
        MDC.put(PROCESS_ID_HEADER_NAME, ProcessIdUtil.checkAndGenerateProcessId(processId));
        log.info("Received /v1/drone/{serialNumber}/medicines request");
        final DroneMedicationDto droneMedicationDto = droneMedicationService.handleCheckLoad(serialNumber);
        return new ResponseEntity<>(droneMedicationDto, HttpStatus.OK);
    }

    @PostMapping("${service.mapping.load-drone-post}")
    public ResponseEntity<String> loadDrone(@RequestHeader(required = false) String processId,
                                            @PathVariable String serialNumber,
                                            @RequestBody MedicationDto medications) {
        MDC.put(PROCESS_ID_HEADER_NAME, ProcessIdUtil.checkAndGenerateProcessId(processId));
        log.info("Received /v1/drone/{serialNumber}/medicines request");
        droneMedicationService.handleLoadRequest(serialNumber, medications);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
