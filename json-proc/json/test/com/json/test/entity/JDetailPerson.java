package com.json.test.entity;

import com.maestro.xml.json.JDomElement;

public class JDetailPerson extends JPerson {

	@JDomElement
	private String surName;
	@JDomElement
	private int year;
	
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}
