package com.example.demo.entity;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String fromPlace;

    @Setter
    private String toPlace;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fromDateTime;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar toDateTime;

    @Setter
    private String transport;

    @Setter
    private double cost;

    @Setter
    private String note;

    @ManyToOne
    private User driver;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Reservation> reservations;

    public Trip(String fromPlace, String toPlace, Calendar fromDateTime,
                Calendar toDateTime, String transport, double cost,
                String note, User driver) {
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.transport = transport;
        this.cost = cost;
        this.note = note;
        this.driver = driver;
    }
}
