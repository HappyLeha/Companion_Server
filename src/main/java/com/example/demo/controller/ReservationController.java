package com.example.demo.controller;
import com.example.demo.dto.RatingDTO;
import com.example.demo.dto.ReservationDTO;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import com.example.demo.exception.NotEnoughRightException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationFailureException;
import com.example.demo.service.ReservationService;
import com.example.demo.service.TripService;
import com.example.demo.service.UserService;
import com.example.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;

@RestController
public class ReservationController {
    private ReservationService reservationService;
    private UserService userService;
    private TripService tripService;

    @Autowired
    public ReservationController(ReservationService reservationService,
                                 UserService userService,
                                 TripService tripService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.tripService = tripService;
    }

    @PostMapping("trips/{id}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public void createReservation(@PathVariable("id") int id,
                                  Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Trip trip = tripService.getTrip(id);

        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) < 0) {
            throw new ValidationFailureException(
                    "You can't reservate this Trip.");
        }
        if (trip.getDriver().equals(user)) {
            throw new NotEnoughRightException(
                    "You can't reservate this Trip.");
        }
        reservationService.createReservation(Mapper.convertToReservation(trip, user));
    }

    @GetMapping("trips/{id}/reservations/single")
    public ReservationDTO getReservation(@PathVariable("id") int id,
                                         Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Trip trip = tripService.getTrip(id);
        Reservation reservation = reservationService.getReservation(user, trip);

        if (trip.getDriver().equals(user)) {
            throw new ResourceNotFoundException(
                    "You are driver of the trip.");
        }
        if (reservation != null) {
            return Mapper.convertToReservationDTO(
                    reservationService.getReservation(user, trip));
        }
        else {
            return new ReservationDTO();
        }
    }

    @GetMapping("trips/{id}/reservations")
    public List<ReservationDTO> getReservations(@PathVariable("id") int id,
                                                Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Trip trip = tripService.getTrip(id);
        List<Reservation> reservations;

        if (!trip.getDriver().equals(user)) {
            throw new NotEnoughRightException(
                    "You can't get these Reservations.");
        }
        reservations = reservationService.getReservations(trip);
        for (Reservation reservation: reservations) {
            userService.calculatePassengerRating(reservation.getPassenger());
        }
        return Mapper.convertToListReservationDTO(reservationService.getReservations(trip));
    }

    @PatchMapping("reservations/{id}/approve")
    public void approveReservation(@PathVariable("id") int id,
                                   Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Reservation reservation = reservationService.getReservation(id);
        Trip trip = reservation.getTrip();

        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) < 0) {
            throw new ValidationFailureException(
                    "You can't approve this Reservation.");
        }
        if (!trip.getDriver().equals(user)) {
            throw new NotEnoughRightException(
                    "You can't approve this Reservation.");
        }
        reservation.setApproved(true);
        reservationService.updateReservation(reservation);
    }

    @PatchMapping("reservations/{id}/ratePassenger")
    public void estimatePassenger(@PathVariable("id") int id,
                                  @RequestBody @Valid RatingDTO ratingDTO,
                                  Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Reservation reservation = reservationService.getReservation(id);
        Trip trip = reservation.getTrip();

        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) > 0) {
            throw new ValidationFailureException(
                    "You can't estimate this Passenger.");
        }
        if (!trip.getDriver().equals(user)) {
            throw new NotEnoughRightException(
                    "You can't estimate this Passenger.");
        }
        reservation.setPassengerRate(ratingDTO.getRating());
        reservationService.updateReservation(reservation);
    }

    @PatchMapping("reservations/{id}/rateDriver")
    public void estimateDriver(@PathVariable("id") int id,
                               @RequestBody @Valid RatingDTO ratingDTO,
                               Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Reservation reservation = reservationService.getReservation(id);
        Trip trip = reservation.getTrip();

        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) > 0) {
            throw new ValidationFailureException(
                    "You can't estimate this Driver.");
        }
        if (!reservation.getPassenger().equals(user)) {
            throw new NotEnoughRightException(
                    "You can't estimate this Passenger.");
        }
        reservation.setDriverRate(ratingDTO.getRating());
        reservationService.updateReservation(reservation);
    }

    @DeleteMapping("reservations/{id}")
    public void deleteReservation(@PathVariable("id") int id, Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        Reservation reservation = reservationService.getReservation(id);
        Trip trip = reservation.getTrip();

        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) < 0) {
            throw new ValidationFailureException(
                    "You can't delete this Reservation.");
        }
        if (!reservation.getPassenger().equals(user)
                && !trip.getDriver().equals(user)) {
            throw new NotEnoughRightException(
                    "You can't delete this Reservation.");
        }
        reservationService.deleteReservation(reservation);
    }
}
