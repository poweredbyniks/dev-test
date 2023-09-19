package com.musala.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @NotBlank
    @JsonProperty(value = "name")
    private String name;

    @NotNull
    @JsonProperty(value = "weight")
    private Integer weight;

    @NotBlank
    @JsonProperty(value = "code")
    private String code;

    @NotNull
    @JsonProperty(value = "image")
    private String image;

}
