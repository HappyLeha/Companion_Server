package com.example.demo.entity;
import java.util.Calendar;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Trip.class)
public abstract class Trip_ {
	public static volatile SingularAttribute<Trip, String> note;
	public static volatile SingularAttribute<Trip, Double> cost;
	public static volatile SingularAttribute<Trip, User> driver;
	public static volatile ListAttribute<Trip, Reservation> reservations;
	public static volatile SingularAttribute<Trip, String> fromPlace;
	public static volatile SingularAttribute<Trip, Calendar> fromDateTime;
	public static volatile SingularAttribute<Trip, String> toPlace;
	public static volatile SingularAttribute<Trip, Integer> id;
	public static volatile SingularAttribute<Trip, String> transport;
	public static volatile SingularAttribute<Trip, Calendar> toDateTime;
}

