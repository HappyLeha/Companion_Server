package com.example.demo.entity;
import lombok.*;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private boolean approved;

    @Setter
    private Integer driverRate;

    @Setter
    private Integer passengerRate;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private User passenger;

    public Reservation(User passenger, Trip trip) {
        this.passenger = passenger;
        this.trip = trip;
    }
}
