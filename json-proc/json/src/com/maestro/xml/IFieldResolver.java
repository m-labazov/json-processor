package com.maestro.xml;

@SuppressWarnings("rawtypes")
public interface IFieldResolver {

	Object convert(Class beanClass, Object value);

	boolean isPrimitive(Object value);
	
}
