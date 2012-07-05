package com.json.test.entity;

import com.maestro.xml.json.JDomElement;

public class JPerson {

	@JDomElement
	private String firstName;
	@JDomElement
	private String lastName;
	@JDomElement
	private JCar car;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public JCar getCar() {
		return car;
	}
	public void setCar(JCar car) {
		this.car = car;
	}
	
	
	
}
