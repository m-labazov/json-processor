package com.json.test.entity;

import com.maestro.json.JsonElement;

public class JCar {

	@JsonElement
	private String name;
	@JsonElement
	private String model;
	@JsonElement
	private int year;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}
