package com.bus.repository;

import com.bus.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RouteRepository
        extends JpaRepository<Route,Long> {

    boolean existsByFromLocationIdAndToLocationId(Long fromLocationId, Long toLocationId);

    @Query("select coalesce(max(r.id), 0) from Route r")
    Long findMaxId();
}
