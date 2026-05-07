package com.bus.repository;

import com.bus.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository
        extends JpaRepository<Trip, Long> {

    @Query("""
            SELECT t
            FROM Trip t
            WHERE t.route.fromLocation.id = :fromId
            AND t.route.toLocation.id = :toId
            """)
    List<Trip> search(Long fromId, Long toId);
    List<Trip>
    findByRoute_FromLocation_IdAndRoute_ToLocation_Id(

            Long fromId,
            Long toId
    );
}