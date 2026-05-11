package com.bus.controller;

import com.bus.dto.SearchTripRequest;
import com.bus.entity.Trip;
import com.bus.repository.LocationRepository;
import com.bus.repository.TripRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final LocationRepository locationRepository;
    private final TripRepository tripRepository;

    // PAGE SEARCH
    @GetMapping("/")
    public String searchPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/buses";
        }

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))) {
            return "redirect:/staff/tickets";
        }

        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("searchRequest", new SearchTripRequest());
        model.addAttribute("trips", List.of());
        return "index";
    }

    // RESULT SEARCH
    @PostMapping("/search")
    public String searchResult(
            @Valid @ModelAttribute("searchRequest") SearchTripRequest request,
            BindingResult result,
            Model model
    ) {

        model.addAttribute("locations", locationRepository.findAll());

        if (request.getFromId() != null
                && request.getToId() != null
                && request.getFromId().equals(request.getToId())) {

            result.rejectValue(
                    "toId",
                    "route.invalid",
                    "Điểm đi và điểm đến không được trùng nhau"
            );
        }

        List<Trip> trips = List.of();

        if (result.hasErrors()) {

            model.addAttribute("trips", trips);

            return "index";
        }

        LocalDateTime start =
                request.getDepartureDate().atStartOfDay();

        LocalDateTime end =
                request.getDepartureDate().atTime(23, 59, 59);

        trips = tripRepository.searchTrips(
                request.getFromId(),
                request.getToId(),
                start,
                end
        );

        model.addAttribute("trips", trips);

        return "index";
    }
}
