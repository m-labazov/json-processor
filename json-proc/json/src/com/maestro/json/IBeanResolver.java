package com.maestro.json;


@SuppressWarnings("rawtypes")
public interface IBeanResolver {
	
	IBeanInfo getBean(Class beanClass) throws JsonException;
	
	IBeanInfo getBean(Class beanClass, String json) throws JsonException;

	void setBeanContext(IBeanInfoContext context);
	
}
