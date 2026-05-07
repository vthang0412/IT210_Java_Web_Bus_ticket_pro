package com.bus.repository;

import com.bus.entity.Seat;
import com.bus.entity.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository
        extends JpaRepository<Seat, Long> {

    List<Seat> findByTripId(Long tripId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT s
            FROM Seat s
            WHERE s.id = :id
            """)
    Optional<Seat> findByIdForUpdate(Long id);
    List<Seat> findByTrip_Id(Long tripId);
}