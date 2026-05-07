package com.bus.repository;

import com.bus.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository
        extends JpaRepository<Route, Long> {

    @Query("""
            SELECT r
            FROM Route r
            JOIN FETCH r.fromLocation
            JOIN FETCH r.toLocation
            """)
    List<Route> findAllRoutes();
}
