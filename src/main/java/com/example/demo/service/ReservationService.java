package com.example.demo.service;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import java.util.List;

public interface ReservationService {
    void createReservation(Reservation reservation);
    Reservation getReservation(User passenger, Trip trip);
    Reservation getReservation(int id);
    List<Reservation> getReservations(Trip trip);
    void updateReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);
}
