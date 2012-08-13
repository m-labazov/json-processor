package com.maestro.json;

public interface IBeanInfoFactory {

	IBeanInfo getBean(Class<?> beanClass) throws JsonException;
	
	IBeanInfo getBean(Class<?> beanClass, String json) throws JsonException;
	
}
