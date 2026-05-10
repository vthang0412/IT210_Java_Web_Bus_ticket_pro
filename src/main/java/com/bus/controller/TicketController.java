package com.bus.controller;

import com.bus.dto.TicketSearchRequest;
import com.bus.repository.TicketRepository;
import com.bus.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;

    private final BookingService bookingService;

    @GetMapping("/ticket/search")
    public String searchPage(Model model) {

        model.addAttribute("ticketSearchRequest", new TicketSearchRequest());
        return "passenger/ticket-search";
    }

    @PostMapping("/ticket/search")
    public String search(

            @Valid @ModelAttribute("ticketSearchRequest") TicketSearchRequest request,

            BindingResult result,
            Model model

    ) {
        if (result.hasErrors()) {
            return "passenger/ticket-search";
        }

        ticketRepository.searchTicket(request.getCode(), request.getPhone())
                .ifPresentOrElse(
                        ticket -> model.addAttribute("ticket", ticket),
                        () -> result.reject("ticket.notFound", "Không tìm thấy vé với mã và số điện thoại đã nhập")
                );

        if (result.hasErrors()) {
            return "passenger/ticket-search";
        }

        return "passenger/ticket-detail";
    }

    @GetMapping("/my-tickets")
    public String myTickets(

            Authentication authentication,

            Model model

    ) {

        model.addAttribute(

                "tickets",

                ticketRepository.findByUserUsername(
                        authentication.getName()
                )
        );

        return "passenger/my-tickets";
    }

    @PostMapping("/ticket/cancel/{id}")
    public String cancel(

            @PathVariable Long id,

            Authentication authentication

    ) {

        bookingService.cancelTicket(

                id,

                authentication.getName()
        );

        return "redirect:/my-tickets";
    }
}
