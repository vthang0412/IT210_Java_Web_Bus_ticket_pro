package com.bus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "bus_type")
    private String busType;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "driver_name")
    private String driverName;
}