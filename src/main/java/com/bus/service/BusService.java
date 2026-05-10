package com.bus.service;

import com.bus.dto.BusRequest;
import com.bus.entity.Bus;
import com.bus.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public void validateBus(BusRequest request) {

        Optional<Bus> existing =
                busRepository.findByLicensePlate(request.getLicensePlate());

        if (existing.isPresent()) {

            Bus bus = existing.get();

            // THÊM MỚI
            if (request.getId() == null) {
                throw new RuntimeException("Biển số đã tồn tại");
            }

            // UPDATE
            if (!bus.getId().equals(request.getId())) {
                throw new RuntimeException("Biển số đã tồn tại");
            }
        }
    }

    public void save(BusRequest request) {

        validateBus(request);

        Bus bus = new Bus();
        bus.setId(request.getId() == null ? busRepository.findMaxId() + 1 : request.getId());
        bus.setLicensePlate(request.getLicensePlate());
        bus.setBusType(request.getBusType());
        bus.setTotalSeats(request.getTotalSeats());
        bus.setCompanyName(request.getCompanyName());
        bus.setDriverName(request.getDriverName());

        busRepository.save(bus);
    }
}
