package com.bus.controller;

import com.bus.entity.TicketStatus;
import com.bus.repository.TicketRepository;
import com.bus.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {

    private final TicketRepository ticketRepository;

    private final StaffService staffService;

    @GetMapping("/tickets")
    public String pendingTickets(Model model) {

        model.addAttribute(

                "tickets",

                ticketRepository.findByStatus(
                        TicketStatus.PENDING
                )
        );

        return "staff/pending-tickets";
    }

    @PostMapping("/approve/{id}")
    public String approve(

            @PathVariable Long id

    ) {

        staffService.approveTicket(id);

        return "redirect:/staff/tickets";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(

            @PathVariable Long id

    ) {

        staffService.cancelTicket(id);

        return "redirect:/staff/tickets";
    }
}
