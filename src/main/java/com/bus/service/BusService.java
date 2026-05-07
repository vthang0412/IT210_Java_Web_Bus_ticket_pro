package com.bus.service;

import com.bus.entity.Bus;
import com.bus.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public Bus save(Bus bus) {
        return busRepository.save(bus);
    }

    public Bus findById(Long id) {
        return busRepository.findById(id)
                .orElseThrow();
    }

    public void delete(Long id) {
        busRepository.deleteById(id);
    }
}
