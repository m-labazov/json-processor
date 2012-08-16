package com.json.test.entity.scan.pack;

import com.maestro.json.JsonElement;

public class TestClassA {

	@JsonElement
	private int fieldA;
	@JsonElement
	private String fieldB;
	
	public int getFieldA() {
		return fieldA;
	}
	public void setFieldA(int fieldA) {
		this.fieldA = fieldA;
	}
	public String getFieldB() {
		return fieldB;
	}
	public void setFieldB(String fieldB) {
		this.fieldB = fieldB;
	}
	
}
