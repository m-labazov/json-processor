package com.maestro.json;

import com.maestro.json.impl.builder.JsonObject;

public interface IBeanProcessor {

	String serialize(Object bean) throws JsonException;
	<T> T deserialize(Class<T> beanClass, String json) throws JsonException;
	<T> T deserialize(Class<T> beanClass, JsonObject jObject) throws JsonException;
	
}
