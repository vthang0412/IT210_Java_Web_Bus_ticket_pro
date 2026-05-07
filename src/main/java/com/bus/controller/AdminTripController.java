package com.bus.controller;

import com.bus.entity.Trip;
import com.bus.repository.BusRepository;
import com.bus.repository.RouteRepository;
import com.bus.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/trips")
@RequiredArgsConstructor
public class AdminTripController {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    @GetMapping
    public String list(Model model){

        model.addAttribute("trips", tripRepository.findAll());

        return "admin/trip-list";
    }

    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("trip", new Trip());
        model.addAttribute("routes", routeRepository.findAll());
        model.addAttribute("buses", busRepository.findAll());

        return "admin/trip-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Trip trip){

        tripRepository.save(trip);

        return "redirect:/admin/trips";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){

        model.addAttribute("trip", tripRepository.findById(id).orElseThrow());
        model.addAttribute("routes", routeRepository.findAll());
        model.addAttribute("buses", busRepository.findAll());

        return "admin/trip-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){

        tripRepository.deleteById(id);

        return "redirect:/admin/trips";
    }
}
