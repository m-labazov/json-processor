package com.maestro.xml;

public interface IFieldResolver {

	Object convert(Class beanClass, Object value);
	
}
