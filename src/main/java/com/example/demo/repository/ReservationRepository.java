package com.example.demo.repository;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    boolean existsByPassengerAndTrip(User user, Trip trip);
    Reservation findByPassengerAndTrip(User passenger, Trip trip);
    Optional<Reservation> findById(int id);
    List<Reservation> findAllByTrip(Trip trip);
}
