package com.json.test.entity;

import java.util.*;

import com.maestro.xml.json.JsonElement;

public class JDepartment {
	
	@JsonElement
	private String name;
	@JsonElement
	private ArrayList<JPerson> personList = new ArrayList<JPerson>();
	@JsonElement
	private JPerson[] personArray;
	@JsonElement
	private JPerson jBoss;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<JPerson> getPersonList() {
		return personList;
	}
	public void setPersonList(ArrayList<JPerson> persons) {
		this.personList = persons;
	}
	
	public void addPerson(JPerson pers) {
		personList.add(pers);
	}
	public JPerson getjBoss() {
		return jBoss;
	}
	public void setjBoss(JPerson jBoss) {
		this.jBoss = jBoss;
	}
	public JPerson[] getPersonArray() {
		return personArray;
	}
	public void setPersonArray(JPerson[] personArray) {
		this.personArray = personArray;
	}
	
}
