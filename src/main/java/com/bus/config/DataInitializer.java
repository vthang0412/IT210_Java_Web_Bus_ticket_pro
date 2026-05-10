package com.bus.config;

import com.bus.entity.Bus;
import com.bus.entity.Location;
import com.bus.entity.Role;
import com.bus.entity.Route;
import com.bus.entity.Seat;
import com.bus.entity.SeatStatus;
import com.bus.entity.Trip;
import com.bus.entity.User;
import com.bus.entity.UserProfile;
import com.bus.repository.BusRepository;
import com.bus.repository.LocationRepository;
import com.bus.repository.RouteRepository;
import com.bus.repository.SeatRepository;
import com.bus.repository.TicketRepository;
import com.bus.repository.TripRepository;
import com.bus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    private final TicketRepository ticketRepository;

    @Override
    public void run(String... args) {

        // =========================
        // RESET DATA
        // =========================

        ticketRepository.deleteAll();

        seatRepository.deleteAll();

        tripRepository.deleteAll();

        // =========================
        // USERS
        // =========================

        createUserIfNotExists(
                "admin",
                "123456",
                Role.ADMIN
        );

        createUserIfNotExists(
                "staff",
                "123456",
                Role.STAFF
        );

        createUserIfNotExists(
                "user",
                "123456",
                Role.PASSENGER
        );

        // =========================
        // LOCATIONS
        // =========================

        if (locationRepository.count() == 0) {

            seedLocations();
        }

        // =========================
        // ROUTES
        // =========================

        if (routeRepository.count() == 0) {

            seedRoutes();
        }

        // =========================
        // BUSES
        // =========================

        if (busRepository.count() == 0) {

            seedBuses();
        }

        // =========================
        // TRIPS
        // =========================

        seedTrips();

        // =========================
        // SEATS
        // =========================

        tripRepository.findAll()
                .forEach(this::createSeatsForTripIfMissing);
    }

    // =========================================================
    // USERS
    // =========================================================

    private void createUserIfNotExists(
            String username,
            String password,
            Role role
    ) {

        userRepository.findByUsername(username)
                .ifPresentOrElse(
                        user -> {
                        },
                        () -> {

                            User user = User.builder()
                                    .username(username)
                                    .password(
                                            passwordEncoder.encode(password)
                                    )
                                    .role(role)
                                    .build();

                            UserProfile profile = new UserProfile();

                            profile.setFullName(username);

                            profile.setUser(user);

                            user.setProfile(profile);

                            userRepository.save(user);
                        }
                );
    }

    // =========================================================
    // LOCATIONS
    // =========================================================

    private void seedLocations() {

        List<String> locations = List.of(

                "Hà Nội",

                "Đà Nẵng",

                "Hồ Chí Minh",

                "Nam Định",

                "Ninh Bình"
        );

        for (String name : locations) {

            Location location = Location.builder()
                    .name(name)
                    .build();

            locationRepository.save(location);
        }
    }

    // =========================================================
    // ROUTES
    // =========================================================

    private void seedRoutes() {

        createRoute(1L, 1L, 2L);

        createRoute(2L, 2L, 3L);

        createRoute(3L, 1L, 3L);

        createRoute(4L, 4L, 3L);
    }

    private void createRoute(
            Long id,
            Long fromLocationId,
            Long toLocationId
    ) {

        if (routeRepository.existsById(id)) {
            return;
        }

        Route route = Route.builder()

                .id(id)

                .fromLocation(
                        locationRepository.findById(fromLocationId)
                                .orElseThrow()
                )

                .toLocation(
                        locationRepository.findById(toLocationId)
                                .orElseThrow()
                )

                .build();

        routeRepository.save(route);
    }

    // =========================================================
    // BUSES
    // =========================================================

    private void seedBuses() {

        if (!busRepository.existsById(1L)) {

            Bus bus1 = Bus.builder()

                    .id(1L)

                    .licensePlate("29B-12345")

                    .busType("Sleeper")

                    .companyName("Hoàng Long")

                    .driverName("Nguyễn Văn A")

                    .totalSeats(20)

                    .build();

            busRepository.save(bus1);
        }

        if (!busRepository.existsById(2L)) {

            Bus bus2 = Bus.builder()

                    .id(2L)

                    .licensePlate("30A-88888")

                    .busType("Seater")

                    .companyName("Futa Bus")

                    .driverName("Trần Văn B")

                    .totalSeats(16)

                    .build();

            busRepository.save(bus2);
        }
    }

    // =========================================================
    // TRIPS
    // =========================================================

    private void seedTrips() {

        createTrip(

                1L,

                1L,

                1L,

                LocalDateTime.now()
                        .plusDays(1)
                        .withHour(8)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0),

                250000.0
        );

        createTrip(

                2L,

                2L,

                1L,

                LocalDateTime.now()
                        .plusDays(1)
                        .withHour(14)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0),

                300000.0
        );

        createTrip(

                3L,

                3L,

                2L,

                LocalDateTime.now()
                        .plusDays(2)
                        .withHour(9)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0),

                350000.0
        );
    }

    private void createTrip(
            Long id,
            Long routeId,
            Long busId,
            LocalDateTime departureTime,
            Double price
    ) {

        Trip trip = Trip.builder()

                .id(id)

                .route(
                        routeRepository.findById(routeId)
                                .orElseThrow()
                )

                .bus(
                        busRepository.findById(busId)
                                .orElseThrow()
                )

                .departureTime(departureTime)

                .price(price)

                .build();

        tripRepository.save(trip);
    }

    // =========================================================
    // SEATS
    // =========================================================

    private void createSeatsForTripIfMissing(Trip trip) {

        if (seatRepository.existsByTripId(trip.getId())) {
            return;
        }

        Long maxId = seatRepository.findMaxId();

        long nextSeatId = (maxId == null) ? 1 : maxId + 1;

        for (int i = 1; i <= trip.getBus().getTotalSeats(); i++) {

            Seat seat = Seat.builder()

                    .id(nextSeatId++)

                    .trip(trip)

                    .seatNumber(String.valueOf(i))

                    .status(SeatStatus.AVAILABLE)

                    .build();

            seatRepository.save(seat);
        }
    }
}