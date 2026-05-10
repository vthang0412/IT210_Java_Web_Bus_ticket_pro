package com.bus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routes")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
        @Id
        private Long id;

        @ManyToOne
        @JoinColumn(name = "from_location_id")
        private Location fromLocation;

        @ManyToOne
        @JoinColumn(name = "to_location_id")
        private Location toLocation;
}
