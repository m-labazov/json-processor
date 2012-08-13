package com.maestro.json;

import java.util.List;

public interface IBeanInfoContext {

	List<IBeanInfo> getBeanInfos();

	void addBeanInfo(IBeanInfo info);
	
	boolean containsBeanInfo(Class<?> clazz);
	
	IBeanInfo getBeanInfo(Class<?> clazz);

}
