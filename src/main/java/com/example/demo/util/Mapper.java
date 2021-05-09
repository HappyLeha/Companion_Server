package com.example.demo.util;
import com.example.demo.dto.*;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Mapper {

    public static User convertToUser(CreateUserDTO createUserDTO) {
        return new User(createUserDTO.getFirstName(), createUserDTO.getLastName(),
                        createUserDTO.getPassword(), createUserDTO.getEmail(),
                        createUserDTO.getPhone(), createUserDTO.getPhoto(),
                        createUserDTO.getNote());
    }

    public static UserDTO convertToUserDTO(User user) {
        CommonRatingDTO commonRatingDTO = convertToCommonRatingDTO(user);
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(),
                           null, user.getEmail(), user.getPhone(),
                           user.getPhoto(), user.getNote(), commonRatingDTO);
    }

    public static Trip convertToTrip(TripDTO tripDTO, User user) {
        return new Trip(tripDTO.getFromPlace(), tripDTO.getToPlace(),
                        tripDTO.getFromDateTime(), tripDTO.getToDateTime(),
                        tripDTO.getTransport(), tripDTO.getCost(),
                        tripDTO.getNote(), user);
    }

    public static TripDTO convertToTripDTO(Trip trip) {
        UserSummaryDTO userSummaryDTO = Mapper.convertToUserSummaryDTO(trip.getDriver());
        return new TripDTO(trip.getId(), trip.getFromPlace(), trip.getToPlace(),
                           trip.getFromDateTime(), trip.getToDateTime(),
                           trip.getTransport(), trip.getCost(), trip.getNote(), userSummaryDTO);
    }

    public static List<TripDTO> convertToListTripDTO(List<Trip> trips) {
        List<TripDTO> tripDTOList = new ArrayList<>();

        for (Trip trip: trips) {
            TripDTO tripDTO = convertToTripDTO(trip);
            tripDTOList.add(tripDTO);
        }
        return tripDTOList;
    }

    public static Reservation convertToReservation(Trip trip, User user) {
        return new Reservation(user, trip);
    }

    public static ReservationDTO convertToReservationDTO (Reservation reservation) {
        return new ReservationDTO(reservation.getId(), reservation.isApproved(),
                                  reservation.getDriverRate(), reservation.getPassengerRate(),
                                   null);
    }

    public static List<ReservationDTO> convertToListReservationDTO(List<Reservation> reservations) {
        List<ReservationDTO> reservationDTOList = new ArrayList<>();

        for (Reservation reservation: reservations) {
            UserSummaryDTO userSummaryDTO = Mapper.convertToUserSummaryDTO(reservation.getPassenger());
            ReservationDTO reservationDTO = new ReservationDTO(reservation.getId(),
                    reservation.isApproved(), reservation.getDriverRate(),
                    reservation.getPassengerRate(), userSummaryDTO);
            reservationDTOList.add(reservationDTO);
        }
        return reservationDTOList;
    }

    public static UserSummaryDTO convertToUserSummaryDTO(User user) {
        CommonRatingDTO commonRatingDTO = convertToCommonRatingDTO(user);
        return new UserSummaryDTO(user.getId(), user.getFirstName(), user.getPhoto(),
                                  commonRatingDTO);
    }

    public static CommonRatingDTO convertToCommonRatingDTO(User user) {
        return new CommonRatingDTO(user.getDriverRating(),user.getPassengerRating());
    }

    public static Calendar convertToCalendar(String string) {
        String[] elements = string.split("-");
        return new GregorianCalendar(Integer.parseInt(elements[0]),
                Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
    }
}
