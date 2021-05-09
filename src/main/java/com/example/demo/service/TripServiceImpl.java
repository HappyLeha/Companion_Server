package com.example.demo.service;
import com.example.demo.dto.TripDTO;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceAlreadyExistException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Trip_;
import com.example.demo.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void createTrip(Trip trip) {
        if (tripRepository.existsByDriverAndFromDateTimeBetween(trip.getDriver(),
                trip.getFromDateTime(), trip.getToDateTime())
                || tripRepository.existsByDriverAndToDateTimeBetween(trip.getDriver(),
                trip.getFromDateTime(), trip.getToDateTime())) {
            throw new ResourceAlreadyExistException("There user has trip on this time.");
        }
        tripRepository.save(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public Trip getTrip(int id) {
        if (!tripRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Trip with this id doesn't exist.");
        }
        return tripRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> getTripsBefore(User user) {
        return tripRepository.findAllByUserBeforeNow(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> getTripsAfter(User user) {
        return tripRepository.findAllByUserAfterNow(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> getTrips(String fromPlace, String toPlace,
                               Calendar startFromDate, Calendar endFromDate,
                               Double cost) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Trip> criteriaQuery = cb.createQuery(Trip.class);
        Root<Trip> tripRoot = criteriaQuery.from(Trip.class);
        Predicate criteria = cb.conjunction();
        List<Trip> result;

        tripRoot.fetch(Trip_.driver, JoinType.LEFT);
        tripRoot.fetch(Trip_.reservations, JoinType.LEFT);
        criteriaQuery.select(tripRoot);
        if (fromPlace != null) {
            Predicate p = cb.like(tripRoot.get(Trip_.fromPlace), "%" + fromPlace + "%");
            criteria = cb.and(criteria, p);
        }
        if (toPlace != null) {
            Predicate p = cb.like(tripRoot.get(Trip_.toPlace), "%" + toPlace + "%");
            criteria = cb.and(criteria, p);
        }
        if (startFromDate != null) {
            Predicate p = cb.greaterThan(tripRoot.get(Trip_.fromDateTime), startFromDate);
            criteria = cb.and(criteria, p);
        }
        if (endFromDate != null) {
            Predicate p = cb.lessThan(tripRoot.get(Trip_.fromDateTime), endFromDate);
            criteria = cb.and(criteria, p);
        }
        if (cost != null) {
            Predicate p = cb.lessThanOrEqualTo(tripRoot.get(Trip_.cost), cost);
            criteria = cb.and(criteria, p);
        }
        criteriaQuery.where(criteria);
        result = em.createQuery(criteriaQuery).getResultList();
        return result;
    }

    @Override
    public void updateTrip(Trip trip, TripDTO tripDTO) {
        if (tripRepository.existsByDriverAndFromDateTimeBetweenAndIdNot(trip.getDriver(),
                tripDTO.getFromDateTime(), tripDTO.getToDateTime(), trip.getId())
                || tripRepository.existsByDriverAndToDateTimeBetweenAndIdNot(trip.getDriver(),
                tripDTO.getFromDateTime(), tripDTO.getToDateTime(), trip.getId())) {
            throw new ResourceAlreadyExistException("There user has trip on this time.");
        }
        trip.setFromPlace(tripDTO.getFromPlace());
        trip.setToPlace(tripDTO.getToPlace());
        trip.setFromDateTime(tripDTO.getFromDateTime());
        trip.setToDateTime(tripDTO.getToDateTime());
        trip.setTransport(tripDTO.getTransport());
        trip.setCost(tripDTO.getCost());
        trip.setNote(tripDTO.getNote());
        tripRepository.save(trip);
    }

    @Override
    public void deleteTrip(Trip trip) {
        tripRepository.delete(trip);
    }
}
