package com.bus.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bus.entity.Seat;
import com.bus.entity.Ticket;
import com.bus.entity.TicketStatus;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingCleanupService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    private final Logger logger = LoggerFactory.getLogger(BookingCleanupService.class);

    // run every minute to release expired pending holds
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void releaseExpiredPending() {

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);

        List<Ticket> expired = ticketRepository.findByStatusAndBookingTimeBefore(
                TicketStatus.PENDING,
                cutoff
        );

        if (expired.isEmpty()) return;

        logger.info("Releasing {} expired pending tickets", expired.size());

        for (Ticket t : expired) {

            try {
                t.setStatus(TicketStatus.CANCELLED);

                Seat seat = t.getSeat();
                if (seat != null) {
                    seat.setStatus(com.bus.entity.SeatStatus.AVAILABLE);
                    seatRepository.save(seat);
                }

                ticketRepository.save(t);

            } catch (Exception ex) {
                logger.warn("Failed to release ticket {}", t.getId(), ex);
            }
        }
    }
}
