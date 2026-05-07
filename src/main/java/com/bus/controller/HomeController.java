package com.bus.controller;

import com.bus.repository.RouteRepository;
import com.bus.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;

    @GetMapping("/")
    public String home(Model model){

        model.addAttribute(
                "routes",
                routeRepository.findAll()
        );

        return "index";
    }

    @GetMapping("/search")
    public String searchTrips(

            @RequestParam(required = false) Long fromId,
            @RequestParam(required = false) Long toId,

            Model model
    ){

        // if parameters are not provided, show the search form (index)
        if(fromId == null || toId == null){
            model.addAttribute(
                    "routes",
                    routeRepository.findAll()
            );
            return "index";
        }

        model.addAttribute(
                "trips",
                tripRepository
                        .findByRoute_FromLocation_IdAndRoute_ToLocation_Id(
                                fromId,
                                toId
                        )
        );

        return "search-result";
    }
}