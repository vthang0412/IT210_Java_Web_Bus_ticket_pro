package com.bus.controller;

import com.bus.dto.BusRequest;
import com.bus.entity.Bus;
import com.bus.repository.BusRepository;
import com.bus.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/buses")
public class AdminBusController {

    private final BusRepository busRepository;
    private final BusService busService;

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("buses", busRepository.findAll());
        return "admin/bus-list";
    }

    // CREATE FORM
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("bus", new BusRequest());
        return "admin/bus-form";
    }

    // SAVE
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("bus") BusRequest request,
                       BindingResult result) {

        // CHECK TRÙNG
        busRepository.findByLicensePlate(request.getLicensePlate())
                .ifPresent(existing -> {
                    if (request.getId() == null ||
                            !existing.getId().equals(request.getId())) {

                        result.rejectValue(
                                "licensePlate",
                                "duplicate",
                                "Biển số đã tồn tại"
                        );
                    }
                });

        // QUAN TRỌNG: phải check lại result
        if (result.hasErrors()) {
            return "admin/bus-form";
        }

        busService.save(request);

        return "redirect:/admin/buses";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        BusRequest dto = new BusRequest();
        dto.setId(bus.getId());
        dto.setLicensePlate(bus.getLicensePlate());
        dto.setBusType(bus.getBusType());
        dto.setTotalSeats(bus.getTotalSeats());
        dto.setCompanyName(bus.getCompanyName());
        dto.setDriverName(bus.getDriverName());

        model.addAttribute("bus", dto);

        return "admin/bus-form";
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        busRepository.deleteById(id);
        return "redirect:/admin/buses";
    }
}
