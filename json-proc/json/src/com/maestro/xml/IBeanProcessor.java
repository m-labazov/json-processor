package com.maestro.xml;

import com.maestro.xml.json.builder.JSONObject;

public interface IBeanProcessor {

	String serialize(Object bean);
	<T> T deserialize(Class<T> beanClass, String json);
	<T> T deserialize(Class<T> beanClass, JSONObject jObject);
	
}
