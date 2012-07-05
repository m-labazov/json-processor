package com.json.test.entity;

import java.util.*;

import com.maestro.xml.json.JDomElement;

public class JDepartment {
	
	@JDomElement
	private String name;
	@JDomElement
	private List<JPerson> persons = new ArrayList<JPerson>();
	@JDomElement
	private JPerson jBoss;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<JPerson> getPersons() {
		return persons;
	}
	public void setPersons(List<JPerson> persons) {
		this.persons = persons;
	}
	
	public void addPerson(JPerson pers) {
		persons.add(pers);
	}
	public JPerson getjBoss() {
		return jBoss;
	}
	public void setjBoss(JPerson jBoss) {
		this.jBoss = jBoss;
	}
	
}
