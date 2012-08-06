package com.json.test.entity;

import com.maestro.xml.json.JDomElement;

public class JStringEntity {

	@JDomElement
	private String str1;
	@JDomElement(cdataField = true)
	private String str2;
	
	public JStringEntity() {}
	
	public JStringEntity(String string, String string2) {
		str1 = string;
		str2 = string2;
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	
	@Override
	public String toString() {
		return str2;
	}
}
