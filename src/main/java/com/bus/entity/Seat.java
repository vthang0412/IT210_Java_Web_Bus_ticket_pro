package com.bus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    private Long id;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
