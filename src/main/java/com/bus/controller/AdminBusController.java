package com.bus.controller;

import com.bus.entity.Bus;
import com.bus.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/buses")
@RequiredArgsConstructor
public class AdminBusController {

    private final BusRepository busRepository;

    @GetMapping
    public String list(Model model){

        model.addAttribute(
                "buses",
                busRepository.findAll()
        );

        return "admin/bus-list";
    }

    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute(
                "bus",
                new Bus()
        );

        return "admin/bus-form";
    }

    @PostMapping("/save")
    public String save(

            @ModelAttribute Bus bus
    ){

        busRepository.save(bus);

        return "redirect:/admin/buses";
    }

    @GetMapping("/edit/{id}")
    public String edit(

            @PathVariable Long id,
            Model model
    ){

        model.addAttribute(
                "bus",
                busRepository.findById(id).orElseThrow()
        );

        return "admin/bus-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(

            @PathVariable Long id
    ){

        busRepository.deleteById(id);

        return "redirect:/admin/buses";
    }
}