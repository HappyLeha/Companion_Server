package com.example.demo.entity;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Reservation.class)
public abstract class Reservation_ {
	public static volatile SingularAttribute<Reservation, Boolean> approved;
	public static volatile SingularAttribute<Reservation, Integer> driverRate;
	public static volatile SingularAttribute<Reservation, Trip> trip;
	public static volatile SingularAttribute<Reservation, User> passenger;
	public static volatile SingularAttribute<Reservation, Integer> id;
	public static volatile SingularAttribute<Reservation, Integer> passengerRate;
}

