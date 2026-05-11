package com.bus.repository;

import com.bus.entity.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;
import java.time.LocalDateTime;

public interface TicketRepository
        extends JpaRepository<Ticket,Long> {

    List<Ticket> findByStatus(
            TicketStatus status
    );

    List<Ticket> findByUserUsername(
            String username
    );
    boolean existsByTripId(Long tripId);
    @Query("""

            select t

            from Ticket t

            join fetch t.trip trip

            join fetch trip.route route

            join fetch trip.bus bus

            join fetch t.seat seat

            where t.ticketCode = :code

            and t.customerPhone = :phone

            """)
    Optional<Ticket> searchTicket(

            @Param("code") String code,

            @Param("phone") String phone
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""

            select t

            from Ticket t

            join fetch t.seat seat

            join fetch t.trip trip

            where t.id = :id

            """)
    Optional<Ticket> lockTicket(@Param("id") Long id);

    @Query("""

            select t

            from Ticket t

            join fetch t.seat seat

            where t.status = :status

            and t.bookingTime < :expiredBefore

            """)
    List<Ticket> findExpiredPendingTickets(
            @Param("status") TicketStatus status,
            @Param("expiredBefore") LocalDateTime expiredBefore
    );
}
