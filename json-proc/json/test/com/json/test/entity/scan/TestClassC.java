package com.json.test.entity.scan;

import com.json.test.entity.scan.pack.TestClassA;
import com.maestro.json.JsonElement;

public class TestClassC {

	@JsonElement
	private String fieldC;
	@JsonElement
	private TestClassA classA;

	public String getFieldC() {
		return fieldC;
	}

	public void setFieldC(String fieldC) {
		this.fieldC = fieldC;
	}

	public TestClassA getClassA() {
		return classA;
	}

	public void setClassA(TestClassA classA) {
		this.classA = classA;
	}
	
}
