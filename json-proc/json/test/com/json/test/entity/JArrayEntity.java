package com.json.test.entity;

import java.util.List;

import com.maestro.json.JsonElement;


public class JArrayEntity {

	@JsonElement(stringValue = true)
	private List<JStringEntity> collection;
	@JsonElement(stringValue = true)
	private JStringEntity[] array;
	@JsonElement(stringValue = true)
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
