package com.maestro.xml;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface IBeanResolver {
	
	IBeanInfo getBean(Class beanClass) throws JsonException;
	
	IBeanInfo getBean(Class beanClass, String json) throws JsonException;

	void setInfos(Map<Class, IBeanInfo> infos);
	
}
