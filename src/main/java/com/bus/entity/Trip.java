package com.bus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @NotNull(message = "Phải chọn tuyến")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    @NotNull(message = "Phải chọn xe")
    private Bus bus;

    @NotNull(message = "Giờ khởi hành không được để trống")
    @FutureOrPresent(message = "Giờ khởi hành phải từ hiện tại trở đi")
    private LocalDateTime departureTime;

    @NotNull(message = "Giá vé không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá vé phải lớn hơn 0")
    private Double price;
}
