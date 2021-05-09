package com.example.demo.entity;
import java.util.Calendar;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Code.class)
public abstract class Code_ {
	public static volatile SingularAttribute<Code, Calendar> expiryDate;
	public static volatile SingularAttribute<Code, String> code;
	public static volatile SingularAttribute<Code, Integer> id;
	public static volatile SingularAttribute<Code, User> user;
}

