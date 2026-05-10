package com.bus.service;

public interface StaffService {

    void approveTicket(Long ticketId);

    void cancelTicket(Long ticketId);
}