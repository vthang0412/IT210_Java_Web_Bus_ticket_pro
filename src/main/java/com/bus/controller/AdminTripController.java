package com.bus.controller;

import com.bus.dto.TripRequest;
import com.bus.entity.Trip;
import com.bus.repository.BusRepository;
import com.bus.repository.RouteRepository;
import com.bus.repository.TicketRepository;
import com.bus.repository.TripRepository;
import com.bus.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/trips")
@RequiredArgsConstructor
public class AdminTripController {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final TripService tripService;
    private final TicketRepository ticketRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("trips", tripRepository.findAll());
        return "admin/trip-list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("trip", new TripRequest());
        model.addAttribute("routes", routeRepository.findAll());
        model.addAttribute("buses", busRepository.findAll());
        return "admin/trip-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("trip") TripRequest request,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("routes", routeRepository.findAll());
            model.addAttribute("buses", busRepository.findAll());
            return "admin/trip-form";
        }

        tripService.save(request);
        return "redirect:/admin/trips";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        TripRequest request = new TripRequest();
        request.setId(trip.getId());
        request.setRouteId(trip.getRoute().getId());
        request.setBusId(trip.getBus().getId());
        request.setDepartureTime(trip.getDepartureTime());
        request.setPrice(trip.getPrice());

        model.addAttribute("trip", request);
        model.addAttribute("routes", routeRepository.findAll());
        model.addAttribute("buses", busRepository.findAll());
        return "admin/trip-form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        if (ticketRepository.existsByTripId(id)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Chuyến đi đã có người đặt vé nên không thể xóa!"
            );

            return "redirect:/admin/trips";
        }

        tripRepository.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Xóa chuyến đi thành công!"
        );

        return "redirect:/admin/trips";
    }
}
