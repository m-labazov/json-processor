package com.maestro.xml.json.resolver;

import java.util.Map;

import com.maestro.xml.IBeanInfo;
import com.maestro.xml.IBeanResolver;

@SuppressWarnings("rawtypes")
public class SimpleBeanResolver implements IBeanResolver {

	private Map<Class, IBeanInfo> beanInfos;
	
	@Override
	public IBeanInfo getBean(Class beanClass) {
		return getBean(beanClass, null);
	}

	@Override
	public IBeanInfo getBean(Class beanClass, String json) {
		IBeanInfo result = beanInfos.get(beanClass);
		return result;
	}

	@Override
	public void setInfos(Map<Class, IBeanInfo> infos) {
		this.beanInfos = infos;
	}

}
