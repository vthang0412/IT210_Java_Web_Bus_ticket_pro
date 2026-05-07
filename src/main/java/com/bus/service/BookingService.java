package com.bus.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bus.entity.Seat;
import com.bus.entity.SeatStatus;
import com.bus.entity.Ticket;
import com.bus.entity.TicketStatus;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import com.bus.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    public Ticket booking(
            Long seatId,
            String customerName,
            String customerPhone,
            String customerEmail,
            String username
    ) {

        Seat seat = seatRepository
                .findByIdForUpdate(seatId)
                .orElseThrow();

        if(seat.getStatus() != SeatStatus.AVAILABLE){
            throw new RuntimeException("Seat unavailable");
        }

        seat.setStatus(SeatStatus.PENDING);

        Ticket ticket = new Ticket();

        ticket.setTicketCode(
                UUID.randomUUID().toString()
        );

        ticket.setCustomerName(customerName);

        ticket.setCustomerPhone(customerPhone);

        ticket.setCustomerEmail(customerEmail);

        ticket.setTrip(seat.getTrip());

        ticket.setSeat(seat);

        ticket.setTotalPrice(
                seat.getTrip().getPrice()
        );

        ticket.setStatus(TicketStatus.PENDING);

        ticket.setBookingTime(
                LocalDateTime.now()
        );

        // link ticket to user if username provided
        if(username != null){
            userRepository.findByUsername(username).ifPresent(ticket::setUser);
        }

        seatRepository.save(seat);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public void approve(Long ticketId){

        Ticket ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow();

        ticket.setStatus(TicketStatus.PAID);

        Seat seat = ticket.getSeat();

        seat.setStatus(SeatStatus.BOOKED);
    }

    @Transactional
    public void cancel(Long ticketId){

        Ticket ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow();

        ticket.setStatus(TicketStatus.CANCELLED);

        Seat seat = ticket.getSeat();

        seat.setStatus(SeatStatus.AVAILABLE);
    }
}