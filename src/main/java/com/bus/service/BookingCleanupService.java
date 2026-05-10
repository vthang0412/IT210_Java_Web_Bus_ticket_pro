package com.bus.service;

import com.bus.entity.SeatStatus;
import com.bus.entity.Ticket;
import com.bus.entity.TicketStatus;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingCleanupService {

    private static final Logger log = LoggerFactory.getLogger(BookingCleanupService.class);

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Scheduled(fixedRate = 10 * 60 * 1000)
    @Transactional
    public void cancelExpiredPendingTickets() {
        LocalDateTime expiredBefore = LocalDateTime.now().minusMinutes(30);
        List<Ticket> tickets = ticketRepository.findExpiredPendingTickets(
                TicketStatus.PENDING,
                expiredBefore
        );

        if (tickets.isEmpty()) {
            log.debug("No expired pending tickets before {}", expiredBefore);
            return;
        }

        for (Ticket ticket : tickets) {
            ticket.setStatus(TicketStatus.CANCELLED);
            ticket.getSeat().setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(ticket.getSeat());
            ticketRepository.save(ticket);
        }

        log.info("Cancelled {} expired pending ticket(s) before {}", tickets.size(), expiredBefore);
    }
}
