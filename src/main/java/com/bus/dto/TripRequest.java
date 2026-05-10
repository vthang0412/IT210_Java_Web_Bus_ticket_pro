package com.bus.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TripRequest {

    private Long id;

    @NotNull(message = "Phải chọn tuyến")
    private Long routeId;

    @NotNull(message = "Phải chọn xe")
    private Long busId;

    @NotNull(message = "Giờ khởi hành không được để trống")
    @FutureOrPresent(message = "Phải từ hiện tại trở đi")
    private LocalDateTime departureTime;

    @NotNull(message = "Giá vé không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải > 0")
    private Double price;
}
