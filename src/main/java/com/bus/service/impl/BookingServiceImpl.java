package com.bus.service.impl;

import com.bus.entity.*;
import com.bus.repository.*;
import com.bus.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final SeatRepository seatRepository;
    private final TripRepository tripRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void bookSeats(
            List<Long> seatIds,
            Long tripId,
            String customerName,
            String customerPhone,
            String customerEmail,
            String username
    ) {

        if (seatIds == null || seatIds.isEmpty()) {
            throw new RuntimeException("No seats selected");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        for (Long seatId : seatIds) {

            Seat seat = seatRepository.lockSeat(seatId)
                    .orElseThrow(() ->
                            new RuntimeException("Seat not found"));

            if (seat.getStatus() != SeatStatus.AVAILABLE) {

                throw new RuntimeException(
                        "Seat " + seat.getSeatNumber()
                                + " is not available"
                );
            }

            if (!seat.getTrip().getId().equals(trip.getId())) {

                throw new RuntimeException(
                        "Seat " + seat.getSeatNumber()
                                + " does not belong to this trip"
                );
            }

            seat.setStatus(SeatStatus.PENDING);

            seatRepository.save(seat);

            Ticket ticket = new Ticket();

            ticket.setTicketCode(
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase()
            );

            ticket.setCustomerName(customerName);
            ticket.setCustomerPhone(customerPhone);
            ticket.setCustomerEmail(customerEmail);

            ticket.setSeat(seat);
            ticket.setTrip(trip);
            ticket.setUser(user);

            ticket.setStatus(TicketStatus.PENDING);

            ticket.setBookingTime(LocalDateTime.now());

            ticketRepository.save(ticket);
        }
    }

    @Override
    @Transactional
    public void cancelTicket(
            Long ticketId,
            String username
    ) {

        Ticket ticket = ticketRepository.lockTicket(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        if (!ticket.getUser()
                .getUsername()
                .equals(username)) {

            throw new RuntimeException("Forbidden");
        }

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            return;
        }

        if (
                ticket.getTrip()
                        .getDepartureTime()
                        .minusHours(12)
                        .isBefore(LocalDateTime.now())
        ) {

            throw new RuntimeException(
                    "Ticket can only be cancelled at least 12 hours before departure"
            );
        }

        ticket.setStatus(TicketStatus.CANCELLED);

        Seat seat = ticket.getSeat();

        seat.setStatus(SeatStatus.AVAILABLE);

        seatRepository.save(seat);

        ticketRepository.save(ticket);
    }
}