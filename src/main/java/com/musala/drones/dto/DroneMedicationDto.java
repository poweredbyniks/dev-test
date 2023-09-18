package com.musala.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DroneMedicationDto implements Serializable {

    @JsonProperty("serialNumber")
    private String serialNumber;

    @JsonProperty("medications")
    private List<Medication> medications;

}
