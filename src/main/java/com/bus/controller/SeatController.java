package com.bus.controller;

import com.bus.repository.SeatRepository;
import com.bus.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class SeatController {

    private final SeatRepository seatRepository;
    private final TripRepository tripRepository;

    @GetMapping("/seats/{tripId}")
    public String seatMap(@PathVariable Long tripId, Model model) {

        model.addAttribute("seats",
                seatRepository.findByTripIdOrderByIdAsc(tripId));

        model.addAttribute("trip",
                tripRepository.findById(tripId).orElseThrow());

        return "passenger/seat-map";
    }
}
