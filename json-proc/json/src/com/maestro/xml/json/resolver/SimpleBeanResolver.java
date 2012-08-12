package com.maestro.xml.json.resolver;

import com.maestro.xml.IBeanInfo;

@SuppressWarnings("rawtypes")
public class SimpleBeanResolver extends AbstractBeanResolver {

	@Override
	public IBeanInfo getBean(Class beanClass) {
		return getBean(beanClass, null);
	}

	@Override
	public IBeanInfo getBean(Class beanClass, String json) {
		IBeanInfo result = beanContext.getBeanInfos().get(beanClass);
		return result;
	}

}
