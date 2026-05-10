package com.bus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)

    private Long id;

    @Column(name = "ticket_code")
    private String ticketCode;

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime bookingTime;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private Seat seat;

    @ManyToOne
    private User user;
}
