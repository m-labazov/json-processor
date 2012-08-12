package com.maestro.xml;


@SuppressWarnings("rawtypes")
public interface IBeanResolver {
	
	IBeanInfo getBean(Class beanClass) throws JsonException;
	
	IBeanInfo getBean(Class beanClass, String json) throws JsonException;

	void setBeanContext(IBeanContext context);
	
}
