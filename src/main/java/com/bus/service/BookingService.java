package com.bus.service;

import java.util.List;

public interface BookingService {

    void bookSeats(

            List<Long> seatIds,

            Long tripId,

            String customerName,

            String customerPhone,

            String customerEmail,

            String username
    );

    void cancelTicket(
            Long ticketId,
            String username
    );
}