package com.bus.repository;

import com.bus.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BusRepository
        extends JpaRepository<Bus,Long> {
    Optional<Bus> findByLicensePlate(String licensePlate);

    @Query("select coalesce(max(b.id), 0) from Bus b")
    Long findMaxId();
}
