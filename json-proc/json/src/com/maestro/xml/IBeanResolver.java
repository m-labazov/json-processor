package com.maestro.xml;

import java.util.Map;

import com.maestro.xml.json.JBeanInfo;

@SuppressWarnings("rawtypes")
public interface IBeanResolver {
	
	JBeanInfo getBean(Class beanClass);
	
	JBeanInfo getBean(Class beanClass, String json);

	void setInfos(Map<Class, JBeanInfo> infos);
	
}
