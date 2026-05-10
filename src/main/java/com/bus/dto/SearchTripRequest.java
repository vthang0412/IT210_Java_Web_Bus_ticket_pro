package com.bus.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SearchTripRequest {

    @NotNull(message = "Vui lòng chọn điểm đi")
    private Long fromId;

    @NotNull(message = "Vui lòng chọn điểm đến")
    private Long toId;

    @NotNull(message = "Vui lòng chọn ngày đi")
    @FutureOrPresent(message = "Ngày đi không được nhỏ hơn ngày hiện tại")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @AssertTrue(message = "Điểm đi và điểm đến không được trùng nhau")
    public boolean isDifferentRoute() {
        return fromId == null || toId == null || !fromId.equals(toId);
    }
}
