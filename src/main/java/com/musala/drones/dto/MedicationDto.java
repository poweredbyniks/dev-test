package com.musala.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MedicationDto implements Serializable {

    @JsonProperty("medications")
    private List<Medication> medications;
}