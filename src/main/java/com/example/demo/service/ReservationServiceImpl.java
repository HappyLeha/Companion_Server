package com.example.demo.service;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceAlreadyExistException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void createReservation(Reservation reservation) {
        if (reservationRepository.existsByPassengerAndTrip(reservation.getPassenger(),
                                                           reservation.getTrip())) {
            throw new ResourceAlreadyExistException("Reservation with that " +
                    "Passenger and Trip already exist.");
        }
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation getReservation(User passenger, Trip trip) {
        return reservationRepository.findByPassengerAndTrip(passenger, trip);
    }

    @Override
    @Transactional
    public Reservation getReservation(int id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (!optionalReservation.isPresent()) {
            throw new ResourceNotFoundException("Reservation with this id doesn't exist.");
        }
        return optionalReservation.get();
    }

    @Override
    @Transactional
    public List<Reservation> getReservations(Trip trip) {
        return reservationRepository.findAllByTrip(trip);
    }

    @Override
    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }
}
