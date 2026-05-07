//package com.bus.controller;
//
//import com.bus.entity.Route;
//import com.bus.repository.LocationRepository;
//import com.bus.repository.RouteRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/admin/routes")
//@RequiredArgsConstructor
//public class AdminRouteController {
//
//    private final RouteRepository routeRepository;
//    private final LocationRepository locationRepository;
//
//    @GetMapping
//    public String list(Model model){
//
//        model.addAttribute("routes", routeRepository.findAllRoutes());
//
//        return "admin/route-list";
//    }
//
//    @GetMapping("/create")
//    public String create(Model model){
//
//        model.addAttribute("route", new Route());
//        model.addAttribute("locations", locationRepository.findAll());
//
//        return "admin/route-form";
//    }
//
//    @PostMapping("/save")
//    public String save(@ModelAttribute Route route){
//
//        routeRepository.save(route);
//
//        return "redirect:/admin/routes";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable Long id, Model model){
//
//        model.addAttribute("route", routeRepository.findById(id).orElseThrow());
//        model.addAttribute("locations", locationRepository.findAll());
//
//        return "admin/route-form";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable Long id){
//
//        routeRepository.deleteById(id);
//
//        return "redirect:/admin/routes";
//    }
//}
