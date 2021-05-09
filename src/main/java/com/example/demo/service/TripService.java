package com.example.demo.service;
import com.example.demo.dto.TripDTO;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import java.util.Calendar;
import java.util.List;

public interface TripService {
    void createTrip(Trip trip);
    Trip getTrip(int id);
    List<Trip> getTripsBefore(User user);
    List<Trip> getTripsAfter(User user);
    List<Trip> getTrips(String fromPlace, String toPlace, Calendar startFromDate,
                        Calendar endFromDate, Double cost);
    void updateTrip(Trip trip, TripDTO tripDTO);
    void deleteTrip(Trip trip);
}
