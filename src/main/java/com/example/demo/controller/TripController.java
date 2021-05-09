package com.example.demo.controller;
import com.example.demo.dto.TripDTO;
import com.example.demo.dto.TripForm;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import com.example.demo.exception.NotEnoughRightException;
import com.example.demo.exception.ValidationFailureException;
import com.example.demo.service.TripService;
import com.example.demo.service.UserService;
import com.example.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trips")
@Validated
public class TripController {
    private TripService tripService;
    private UserService userService;

    @Autowired
    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrip(@RequestBody @Valid TripDTO tripDTO, Principal principal) {
        User user;

        if (tripDTO.getFromDateTime().compareTo(Calendar.getInstance())<0
                ||tripDTO.getToDateTime().compareTo(tripDTO.getFromDateTime())<0) {
            throw new ValidationFailureException(
                    "fromDateTime or toDateTime aren't valid.");
        }
        user = userService.loadUserByUsername(principal.getName());

        tripService.createTrip(Mapper.convertToTrip(tripDTO, user));
    }

    @GetMapping("/{id}")
    public TripDTO getTrip(@PathVariable("id") int id) {
        Trip trip = tripService.getTrip(id);

        userService.calculateDriverRating(trip.getDriver());
        return Mapper.convertToTripDTO(trip);
    }

    @GetMapping("/before")
    public List<TripDTO> getTripsBefore(Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        List<Trip> trips = tripService.getTripsBefore(user);

        for (Trip trip: trips) {
            userService.calculateDriverRating(trip.getDriver());
        }
        return Mapper.convertToListTripDTO(trips);
    }

    @GetMapping("/after")
    public List<TripDTO> getTripsAfter(Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());
        List<Trip> trips = tripService.getTripsAfter(user);

        for (Trip trip: trips) {
            userService.calculateDriverRating(trip.getDriver());
        }
        return Mapper.convertToListTripDTO(trips);
    }

    @PostMapping("/search")
    public List<TripDTO> searchTrips(@RequestBody @Valid TripForm tripForm) {
        Calendar calendarStartFromDate = Calendar.getInstance();
        Calendar calendarEndFromDate = null;
        List<Trip> trips;

        if (tripForm.getStartFromDate() !=  null) {
            calendarStartFromDate = tripForm.getStartFromDate();
        }
        if (tripForm.getEndFromDate() !=  null) {
            calendarEndFromDate = tripForm.getEndFromDate();
        }
        trips = tripService.getTrips(tripForm.getFrom(), tripForm.getTo(),
                                     calendarStartFromDate,
                                     calendarEndFromDate, tripForm.getCost());
        if (tripForm.getRating() != null) {
            for (Trip trip : trips) {
                userService.calculateDriverRating(trip.getDriver());
            }
            trips = trips.stream().filter(trip -> trip.getDriver().getDriverRating() != null
                                          && trip.getDriver().getDriverRating()
                                          >= tripForm.getRating())
                    .collect(Collectors.toList());
        }
        return Mapper.convertToListTripDTO(trips);
    }

    @PutMapping("/{id}")
    public void updateTrip(@PathVariable("id") int id,
                           @RequestBody @Valid TripDTO tripDTO,
                           Principal principal) {
        User user;
        Trip trip;

        if (tripDTO.getFromDateTime().compareTo(Calendar.getInstance()) < 0
                ||tripDTO.getToDateTime().compareTo(tripDTO.getFromDateTime()) < 0) {
            throw new ValidationFailureException(
                    "fromDateTime or toDateTime aren't valid.");
        }
        trip = tripService.getTrip(id);
        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) < 0) {
            throw new ValidationFailureException(
                    "You can't change this Trip.");
        }
        user = userService.loadUserByUsername(principal.getName());
        if (!trip.getDriver().equals(user)) {
            throw new NotEnoughRightException("You can't change this Trip");
        }
        tripService.updateTrip(trip, tripDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTrip(@PathVariable("id") int id,
                           Principal principal) {
        User user;
        Trip trip;

        trip = tripService.getTrip(id);
        if (trip.getFromDateTime().compareTo(Calendar.getInstance()) > 0) {
            throw new ValidationFailureException(
                    "You can't delete this Trip.");
        }
        user = userService.loadUserByUsername(principal.getName());
        if (!trip.getDriver().equals(user)) {
            throw new NotEnoughRightException("You can't delete this Trip");
        }
        tripService.deleteTrip(trip);
    }
}
