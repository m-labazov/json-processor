package com.maestro.json;

@SuppressWarnings("rawtypes")
public interface IFieldResolver {

	Object convertToObject(Class beanClass, Object value);

	String convertFromObject(Object value);

	boolean isPrimitive(Object value);
	
}
