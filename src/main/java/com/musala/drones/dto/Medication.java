package com.musala.drones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "weight")
    private Integer weight;

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "image")
    private byte[] image;

}
