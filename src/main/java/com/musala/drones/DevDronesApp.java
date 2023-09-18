package com.musala.drones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DevDronesApp {
    public static void main(String[] args) {
        SpringApplication.run(DevDronesApp.class, args);
    }
}