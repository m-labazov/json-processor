package com.json.test.entity;

import java.util.*;

import com.maestro.xml.json.JDomElement;

public class JDepartment {
	
	@JDomElement
	private String name;
	@JDomElement
	private HashSet<JPerson> persons = new HashSet<JPerson>();
	@JDomElement
	private JPerson jBoss;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashSet<JPerson> getPersons() {
		return persons;
	}
	public void setPersons(HashSet<JPerson> persons) {
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
