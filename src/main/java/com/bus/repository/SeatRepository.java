package com.bus.repository;

import com.bus.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface SeatRepository
        extends JpaRepository<Seat,Long> {

    List<Seat> findByTripId(Long tripId);

    List<Seat> findByTripIdOrderByIdAsc(Long tripId);

    boolean existsByTripId(Long tripId);

    @Query("select coalesce(max(s.id), 0) from Seat s")
    Long findMaxId();

    @Lock(LockModeType.PESSIMISTIC_WRITE)

    @Query("""

            select s

            from Seat s

            where s.id = :id

            """)
    Optional<Seat> lockSeat(

            @Param("id") Long id
    );
}
