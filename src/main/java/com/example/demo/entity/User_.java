package com.example.demo.entity;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public abstract class User_ {
	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, String> note;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, Code> code;
	public static volatile ListAttribute<User, Reservation> reservations;
	public static volatile SingularAttribute<User, String> phone;
	public static volatile ListAttribute<User, Trip> trips;
	public static volatile SingularAttribute<User, String> photo;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> email;
}

