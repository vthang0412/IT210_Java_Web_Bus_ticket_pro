package com.bus.controller;

import com.bus.dto.BookingRequest;
import com.bus.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/booking")
    public String bookingForm(
            @RequestParam String seatIds,
            @RequestParam Long tripId,
            @ModelAttribute("bookingRequest") BookingRequest request
    ) {

        request.setTripId(tripId);

        request.setSeatIds(
                java.util.Arrays.stream(seatIds.split(","))
                        .map(Long::valueOf)
                        .toList()
        );

        return "passenger/booking-form";
    }

    @PostMapping("/booking")
    public String booking(
            @Valid @ModelAttribute("bookingRequest") BookingRequest request,
            BindingResult result,
            Authentication auth
    ) {

        if (result.hasErrors()) {
            return "passenger/booking-form";
        }

        bookingService.bookSeats(
                request.getSeatIds(),
                request.getTripId(),
                request.getCustomerName(),
                request.getCustomerPhone(),
                request.getCustomerEmail(),
                auth.getName()
        );

        return "redirect:/my-tickets";
    }
}