package com.json.test.entity;

import java.util.List;

import com.maestro.xml.json.JDomElement;


public class JArrayEntity {

	@JDomElement(stringValue = true)
	private List<JStringEntity> collection;
	@JDomElement(stringValue = true)
	private JStringEntity[] array;
	@JDomElement(stringValue = true)
	private JStringEntity entity;
	
	public List<JStringEntity> getCollection() {
		return collection;
	}
	public void setCollection(List<JStringEntity> collection) {
		this.collection = collection;
	}
	public JStringEntity[] getArray() {
		return array;
	}
	public void setArray(JStringEntity[] array) {
		this.array = array;
	}
	public JStringEntity getEntity() {
		return entity;
	}
	public void setEntity(JStringEntity entity) {
		this.entity = entity;
	}
	
	
}
