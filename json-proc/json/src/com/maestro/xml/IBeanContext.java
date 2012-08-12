package com.maestro.xml;

import java.util.Map;

public interface IBeanContext {

	Map<Class<?>, IBeanInfo> getBeanInfos();

	void addBeanInfo(IBeanInfo info);
	
	boolean containsBeanInfo(Class<?> clazz);

}
