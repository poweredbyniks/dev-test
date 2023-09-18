package com.musala.drones.repository;

import com.musala.drones.model.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, Long> {

    @Query(value = "SELECT DroneEntity FROM DroneEntity drone WHERE drone.serialNumber =:serialNumber")
    Optional<DroneEntity> findDroneEntityBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query(value = "SELECT DroneEntity FROM DroneEntity drone WHERE drone.state =:state")
    List<DroneEntity> findAllByState(@Param("state") String state);

}
