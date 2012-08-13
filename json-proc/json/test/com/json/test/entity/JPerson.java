package com.json.test.entity;

import com.maestro.json.JsonElement;

public class JPerson {

	@JsonElement
	private String firstName;
	@JsonElement
	private String lastName;
	@JsonElement
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
