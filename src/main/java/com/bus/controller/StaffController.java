package com.bus.controller;

import com.bus.entity.Seat;
import com.bus.entity.Ticket;
import com.bus.entity.SeatStatus;
import com.bus.entity.TicketStatus;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff/tickets")
@RequiredArgsConstructor
public class StaffController {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @GetMapping
    public String pendingTickets(Model model){

        model.addAttribute(
                "tickets",
                ticketRepository.findByStatus(
                        TicketStatus.PENDING
                )
        );

        return "staff/staff-ticket-list";
    }

    @GetMapping("/approve/{id}")
    @Transactional
    public String approve(

            @PathVariable Long id
    ){

        Ticket ticket =
                ticketRepository.findById(id)
                        .orElseThrow();

        ticket.setStatus(TicketStatus.PAID);

        Seat seat = ticket.getSeat();

        seat.setStatus(SeatStatus.BOOKED);

        seatRepository.save(seat);
        ticketRepository.save(ticket);

        return "redirect:/staff/tickets";
    }

    @GetMapping("/cancel/{id}")
    @Transactional
    public String cancel(

            @PathVariable Long id
    ){

        Ticket ticket =
                ticketRepository.findById(id)
                        .orElseThrow();

        ticket.setStatus(TicketStatus.CANCELLED);

        Seat seat = ticket.getSeat();

        seat.setStatus(SeatStatus.AVAILABLE);

        seatRepository.save(seat);
        ticketRepository.save(ticket);

        return "redirect:/staff/tickets";
    }
}