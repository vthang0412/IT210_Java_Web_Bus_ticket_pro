package com.bus.repository;

import com.bus.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    boolean existsByBusIdAndDepartureTime(Long busId, LocalDateTime departureTime);

    @Query("select coalesce(max(t.id), 0) from Trip t")
    Long findMaxId();

        @Query("""
        SELECT t FROM Trip t
        JOIN t.route r
        WHERE r.fromLocation.id = :fromId
          AND r.toLocation.id = :toId
          AND t.departureTime BETWEEN :start AND :end
    """)
        List<Trip> searchTrips(Long fromId, Long toId,
                               LocalDateTime start,
                               LocalDateTime end);

}
