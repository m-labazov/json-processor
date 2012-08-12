package com.maestro.xml.json;

import java.util.HashMap;
import java.util.Map;

import com.maestro.xml.IBeanContext;
import com.maestro.xml.IBeanInfo;

public class DefaultBeanContext implements IBeanContext {

	// TODO possibly needs synchronization
	private Map<Class<?>, IBeanInfo> beanInfos;

	public DefaultBeanContext() {
		beanInfos = new HashMap<Class<?>, IBeanInfo>();
	}
	
	@Override
	public void addBeanInfo(IBeanInfo info) {
		beanInfos.put(info.getBeanClass(), info);
	}
	
	@Override
	public Map<Class<?>, IBeanInfo> getBeanInfos() {
		return beanInfos;
	}

	public void setBeanInfos(Map<Class<?>, IBeanInfo> beanInfos) {
		this.beanInfos = beanInfos;
	}

	@Override
	public boolean containsBeanInfo(Class<?> clazz) {
		return beanInfos.containsKey(clazz);
	}
	
}
