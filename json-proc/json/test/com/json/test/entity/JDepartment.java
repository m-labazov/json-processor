package com.json.test.entity;

import java.util.*;

import com.maestro.xml.json.JDomElement;

public class JDepartment {
	
	@JDomElement
	private String name;
	@JDomElement
	private HashSet<JPerson> personList = new HashSet<JPerson>();
	@JDomElement
	private JPerson[] personArray;
	@JDomElement
	private JPerson jBoss;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashSet<JPerson> getPersonList() {
		return personList;
	}
	public void setPersonList(HashSet<JPerson> persons) {
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
