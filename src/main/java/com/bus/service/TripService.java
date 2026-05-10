package com.bus.service;

import com.bus.dto.TripRequest;
import com.bus.entity.Seat;
import com.bus.entity.SeatStatus;
import com.bus.entity.Trip;
import com.bus.repository.BusRepository;
import com.bus.repository.RouteRepository;
import com.bus.repository.SeatRepository;
import com.bus.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void save(TripRequest request) {
        Trip trip = request.getId() == null
                ? new Trip()
                : tripRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (request.getId() == null) {
            trip.setId(tripRepository.findMaxId() + 1);
        }

        trip.setRoute(routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found")));
        trip.setBus(busRepository.findById(request.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found")));
        trip.setDepartureTime(request.getDepartureTime());
        trip.setPrice(request.getPrice());

        Trip savedTrip = tripRepository.save(trip);

        if (!seatRepository.existsByTripId(savedTrip.getId())) {
            long nextSeatId = seatRepository.findMaxId() + 1;
            for (int i = 1; i <= savedTrip.getBus().getTotalSeats(); i++) {
                Seat seat = Seat.builder()
                        .id(nextSeatId++)
                        .trip(savedTrip)
                        .seatNumber(String.valueOf(i))
                        .status(SeatStatus.AVAILABLE)
                        .build();
                seatRepository.save(seat);
            }
        }
    }
}
