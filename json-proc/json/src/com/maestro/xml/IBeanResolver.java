package com.maestro.xml;

import java.util.Map;

import com.maestro.xml.json.JBeanInfo;

@SuppressWarnings("rawtypes")
public interface IBeanResolver {
	
	JBeanInfo getBean(Class beanClass) throws JsonException;
	
	JBeanInfo getBean(Class beanClass, String json) throws JsonException;

	void setInfos(Map<Class, JBeanInfo> infos);
	
}
