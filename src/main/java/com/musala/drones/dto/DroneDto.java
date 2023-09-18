package com.musala.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musala.drones.model.Model;
import com.musala.drones.model.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DroneDto implements Serializable {

    @NotBlank(message = "serialNumber is required")
    @Size(max = 100, message = "serialNumber must be less than 100 characters long")
    @JsonProperty(value = "serialNumber")
    private String serialNumber;

    @NotBlank(message = "model is required")
    @JsonProperty(value = "model")
    private Model model;

    @DecimalMax(value = "500", inclusive = true, message = "weightLimit must be equal or less than 500gr")
    @NotBlank(message = "weightLimit is required")
    @JsonProperty(value = "weightLimit")
    private Integer weightLimit;

    @NotBlank(message = "batteryCapacity is required")
    @DecimalMax(value = "500", inclusive = true, message = "batteryCapacity must be equal or less than 100 %")
    @JsonProperty(value = "batteryCapacity")
    private Integer batteryCapacity;

    @NotBlank(message = "state is required")
    @JsonProperty(value = "state")
    private State state;

}