package com.json.test.entity;

import com.maestro.xml.json.JDomElement;

public class JEnumEntity {

	@JDomElement
	private EnumEntity ent1;
	@JDomElement
	private EnumEntity ent2;
	@JDomElement
	private EnumEntity ent3;
	public EnumEntity getEnt1() {
		return ent1;
	}
	public void setEnt1(EnumEntity ent1) {
		this.ent1 = ent1;
	}
	public EnumEntity getEnt2() {
		return ent2;
	}
	public void setEnt2(EnumEntity ent2) {
		this.ent2 = ent2;
	}
	public EnumEntity getEnt3() {
		return ent3;
	}
	public void setEnt3(EnumEntity ent3) {
		this.ent3 = ent3;
	}
	
}
