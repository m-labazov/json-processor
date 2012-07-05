package com.maestro.xml.json.resolver;

import java.util.Map;

import com.maestro.xml.IBeanResolver;
import com.maestro.xml.json.JBeanInfo;

@SuppressWarnings("rawtypes")
public class SimpleBeanResolver implements IBeanResolver {

	private Map<Class, JBeanInfo> beanInfos;
	
	@Override
	public JBeanInfo getBean(Class beanClass) {
		return getBean(beanClass, null);
	}

	@Override
	public JBeanInfo getBean(Class beanClass, String json) {
		JBeanInfo result = beanInfos.get(beanClass);
		return result;
	}

	@Override
	public void setInfos(Map<Class, JBeanInfo> infos) {
		this.beanInfos = infos;
	}

}
