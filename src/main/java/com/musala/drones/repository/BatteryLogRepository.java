package com.musala.drones.repository;

import com.musala.drones.model.BatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryLogRepository extends JpaRepository<BatteryLog, Long> {
}
