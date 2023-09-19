package com.musala.drones.repository;

import com.musala.drones.model.DroneEntity;
import com.musala.drones.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM drones")
    List<DroneEntity> findAllEntities();

    @Query(value = "SELECT drone FROM DroneEntity drone WHERE drone.serialNumber =:serialNumber")
    Optional<DroneEntity> findDroneEntityBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query(value = "SELECT drone FROM DroneEntity drone WHERE drone.state =:state")
    List<DroneEntity> findAllByState(@Param("state") State state);

}
