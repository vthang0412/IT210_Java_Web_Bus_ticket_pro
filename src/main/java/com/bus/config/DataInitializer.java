package com.bus.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bus.entity.Bus;
import com.bus.entity.Location;
import com.bus.entity.Role;
import com.bus.entity.Route;
import com.bus.entity.Seat;
import com.bus.entity.SeatStatus;
import com.bus.entity.Trip;
import com.bus.entity.User;
import com.bus.repository.BusRepository;
import com.bus.repository.LocationRepository;
import com.bus.repository.RouteRepository;
import com.bus.repository.SeatRepository;
import com.bus.repository.TripRepository;
import com.bus.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LocationRepository locationRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }

        if (userRepository.findByUsername("staff").isEmpty()) {

            User staff = User.builder()
                    .username("staff")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.STAFF)
                    .build();

            userRepository.save(staff);
        }

            // Seed locations and routes if not present
            if(locationRepository.count() == 0){

                Location hanoi = Location.builder().name("Hanoi").build();
                Location hcm = Location.builder().name("HCMC").build();
                Location danang = Location.builder().name("Da Nang").build();

                locationRepository.save(hanoi);
                locationRepository.save(hcm);
                locationRepository.save(danang);

                Route r1 = Route.builder()
                    .fromLocation(hanoi)
                    .toLocation(danang)
                    .distanceKm(800.0)
                    .build();

                Route r2 = Route.builder()
                    .fromLocation(danang)
                    .toLocation(hcm)
                    .distanceKm(960.0)
                    .build();

                routeRepository.save(r1);
                routeRepository.save(r2);

                // Seed a bus and trips/seats for demo
                if(busRepository.count() == 0){

                    Bus bus = Bus.builder()
                        .licensePlate("29A-11111")
                        .busType("Sleeper")
                        .companyName("DemoBusCo")
                        .driverName("Nguyen Van A")
                        .totalSeats(12)
                        .build();

                    busRepository.save(bus);

                    // create trips for the two routes
                    Trip t1 = Trip.builder()
                        .route(r1)
                        .bus(bus)
                        .departureTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0))
                        .price(250000.0)
                        .build();

                    Trip t2 = Trip.builder()
                        .route(r2)
                        .bus(bus)
                        .departureTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0))
                        .price(300000.0)
                        .build();

                    tripRepository.save(t1);
                    tripRepository.save(t2);

                    // create seats for each trip
                    for(int i=1;i<=bus.getTotalSeats();i++){
                    Seat s1 = Seat.builder()
                        .trip(t1)
                        .seatNumber(String.valueOf(i))
                        .status(SeatStatus.AVAILABLE)
                        .build();

                    Seat s2 = Seat.builder()
                        .trip(t2)
                        .seatNumber(String.valueOf(i))
                        .status(SeatStatus.AVAILABLE)
                        .build();

                    seatRepository.save(s1);
                    seatRepository.save(s2);
                    }
                }
            }
    }
}