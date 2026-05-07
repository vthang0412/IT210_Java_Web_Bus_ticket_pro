package com.bus.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bus.entity.Ticket;
import com.bus.entity.TicketStatus;

public interface TicketRepository
        extends JpaRepository<Ticket,Long> {

    Optional<Ticket>
    findByTicketCodeAndCustomerPhone(
            String code,
            String phone
    );

    List<Ticket> findByStatus(
            TicketStatus status
    );

    List<Ticket> findByStatusAndBookingTimeBefore(
            TicketStatus status,
            LocalDateTime time
    );
}