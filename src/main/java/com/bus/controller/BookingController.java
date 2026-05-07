package com.bus.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import com.bus.service.BookingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookingController {

        private final SeatRepository seatRepository;
        private final TicketRepository ticketRepository;
        private final BookingService bookingService;

    @GetMapping("/trips/{tripId}/seats")
    public String seatMap(

            @PathVariable Long tripId,
            Model model
    ){

        model.addAttribute(
                "seats",
                seatRepository.findByTrip_Id(tripId)
        );

        return "passenger/seat-map";
    }

    @GetMapping("/booking")
    public String bookingForm(

            @RequestParam Long seatId,
            Model model
    ){

        model.addAttribute("seatId", seatId);

        return "passenger/booking-form";
    }

    @PostMapping("/booking")
    @Transactional
    public String booking(

            @RequestParam Long seatId,
            @RequestParam String customerName,
            @RequestParam String customerPhone,
            @RequestParam String customerEmail,
            Authentication authentication
    ){

        // delegate to BookingService which handles transaction and locking
        try {
            // Only PASSENGER or STAFF allowed to create bookings
            if(authentication == null || !(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PASSENGER"))
                    || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF")))){
                throw new AccessDeniedException("Not allowed to book");
            }
                var ticket = bookingService.booking(
                    seatId,
                    customerName,
                    customerPhone,
                    customerEmail,
                    authentication == null ? null : authentication.getName()
                );

            return "redirect:/ticket/detail/" + ticket.getId();

        } catch (Exception ex){
            // booking failed (seat unavailable or other), redirect back to home/search
            return "redirect:/";
        }
    }
}