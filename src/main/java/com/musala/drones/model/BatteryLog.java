package com.musala.drones.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "battery_logs")
public class BatteryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_date_time", columnDefinition = "TIMESTAMP")
    private ZonedDateTime logDateTime;

    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

}
