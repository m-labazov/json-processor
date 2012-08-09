package com.maestro.xml;

import com.maestro.xml.json.builder.JsonObject;

public interface IBeanProcessor {

	String serialize(Object bean) throws JsonException;
	<T> T deserialize(Class<T> beanClass, String json) throws JsonException;
	<T> T deserialize(Class<T> beanClass, JsonObject jObject) throws JsonException;
	
}
