package com.musala.drones.config;

import com.musala.drones.model.MedicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationEntityRepository extends JpaRepository<MedicationEntity, Long> {
}