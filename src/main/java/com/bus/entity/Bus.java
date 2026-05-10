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
    private Long id;

    @Column(unique = true)
    private String licensePlate;

    private String busType;

    private Integer totalSeats;

    private String companyName;

    private String driverName;
}
