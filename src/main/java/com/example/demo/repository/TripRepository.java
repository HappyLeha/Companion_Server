package com.example.demo.repository;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Calendar;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Integer> {

    boolean existsByDriverAndFromDateTimeBetween(User user, Calendar start, Calendar end);

    boolean existsByDriverAndToDateTimeBetween(User user, Calendar start, Calendar end);

    boolean existsByDriverAndFromDateTimeBetweenAndIdNot(User user, Calendar start, Calendar end, int id);

    boolean existsByDriverAndToDateTimeBetweenAndIdNot(User user, Calendar start, Calendar end, int id);

    @Query("select t from Trip t, Reservation r where (t.driver = :user or " +
            "(r.passenger = :user and r.trip = t)) and t.fromDateTime < current_timestamp")
    List<Trip> findAllByUserBeforeNow(@Param("user") User user);

    @Query("select t from Trip t, Reservation r where (t.driver = :user or " +
            "(r.passenger = :user and r.trip = t)) and t.fromDateTime > current_timestamp")
    List<Trip> findAllByUserAfterNow(@Param("user") User user);
}
