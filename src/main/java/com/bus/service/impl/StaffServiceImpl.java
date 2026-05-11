package com.bus.service.impl;

import com.bus.entity.Seat;
import com.bus.entity.SeatStatus;
import com.bus.entity.Ticket;
import com.bus.entity.TicketStatus;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import com.bus.service.StaffService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final TicketRepository ticketRepository;

    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public void approveTicket(Long ticketId) {

        Ticket ticket = ticketRepository
                .lockTicket(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Chỉ những vé đang chờ xử lý mới được chấp thuận");
        }

        ticket.setStatus(
                TicketStatus.PAID
        );

        Seat seat = ticket.getSeat();

        seat.setStatus(
                SeatStatus.BOOKED
        );

        seatRepository.save(seat);

        ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public void cancelTicket(Long ticketId) {

        Ticket ticket = ticketRepository
                .lockTicket(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            return;
        }

        ticket.setStatus(
                TicketStatus.CANCELLED
        );

        Seat seat = ticket.getSeat();

        seat.setStatus(
                SeatStatus.AVAILABLE
        );

        seatRepository.save(seat);

        ticketRepository.save(ticket);
    }
}
