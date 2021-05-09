package com.example.demo.repository;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select avg(r.driverRate) from Reservation r where r.trip.driver = :user " +
            "group by r.trip.driver")
    Double calculateDriverRatingByUser(@Param("user") User user);

    @Query("select avg(r.passengerRate) from Reservation r where r.passenger = :user " +
            "group by r.passengerRate")
    Double calculatePassengerRatingByUser(@Param("user") User user);
}
