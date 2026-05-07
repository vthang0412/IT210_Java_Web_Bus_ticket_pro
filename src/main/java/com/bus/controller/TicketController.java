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

import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @GetMapping("/ticket/search")
    public String searchPage(){

                return "passenger/ticket-search";
    }

    @PostMapping("/ticket/search")
    public String searchTicket(

            @RequestParam String code,
            @RequestParam String phone
    ){

        Ticket ticket =
                ticketRepository
                        .findByTicketCodeAndCustomerPhone(
                                code,
                                phone
                        )
                        .orElse(null);

        if(ticket == null){
            return "redirect:/ticket/search";
        }

        return "redirect:/ticket/detail/"
                + ticket.getId();
    }

    @GetMapping("/ticket/detail/{id}")
    public String ticketDetail(

            @PathVariable Long id,
            Model model
    ){

        model.addAttribute(
                "ticket",
                ticketRepository.findById(id).orElseThrow()
        );

                return "passenger/ticket-detail";
    }

    @GetMapping("/ticket/cancel/{id}")
    @Transactional
    public String cancelTicket(

            @PathVariable Long id,
            org.springframework.security.core.Authentication authentication
    ){

        Ticket ticket =
                ticketRepository.findById(id)
                        .orElseThrow();

        // Only allow staff/admin or ticket owner to cancel
        // (owner recorded in ticket.user)
        // If not authorized, redirect to detail

        LocalDateTime departure =
                ticket.getTrip().getDepartureTime();

        long hours =
                Duration.between(
                        LocalDateTime.now(),
                        departure
                ).toHours();


                // authorization: staff/admin can cancel any; passengers can cancel only their own ticket
                boolean isStaffOrAdmin = authentication != null && authentication.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF") || a.getAuthority().equals("ROLE_ADMIN"));

                boolean isOwner = false;
                if(authentication != null && ticket.getUser() != null){
                        isOwner = authentication.getName().equals(ticket.getUser().getUsername());
                }

                if(!isStaffOrAdmin && !isOwner){
                        return "redirect:/ticket/detail/" + id;
                }

                if(hours < 12 && !isStaffOrAdmin){
                        // only staff/admin can cancel within 12h
                        return "redirect:/ticket/detail/" + id;
                }

        ticket.setStatus(TicketStatus.CANCELLED);

        Seat seat = ticket.getSeat();

        seat.setStatus(SeatStatus.AVAILABLE);

        seatRepository.save(seat);
        ticketRepository.save(ticket);

        return "redirect:/ticket/detail/" + id;
    }
}