package com.bus.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusRequest {

    private Long id;

    @NotBlank(message = "Biển số không được để trống")
    private String licensePlate;

    @NotBlank(message = "Loại xe không được để trống")
    private String busType;

    @NotNull(message = "Số ghế không được để trống")
    @Min(value = 10, message = "Số ghế tối thiểu 10")
    @Max(value = 60, message = "Số ghế tối đa 60")
    private Integer totalSeats;

    @NotBlank(message = "Hãng xe không được để trống")
    private String companyName;

    @NotBlank(message = "Tài xế không được để trống")
    private String driverName;
}
