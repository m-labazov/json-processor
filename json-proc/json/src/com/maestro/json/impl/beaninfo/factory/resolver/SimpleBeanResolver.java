package com.maestro.json.impl.beaninfo.factory.resolver;

import com.maestro.json.IBeanInfo;

@SuppressWarnings("rawtypes")
public class SimpleBeanResolver extends AbstractBeanResolver {

	@Override
	public IBeanInfo getBean(Class beanClass) {
		return getBean(beanClass, null);
	}

	@Override
	public IBeanInfo getBean(Class beanClass, String json) {
		IBeanInfo result = beanContext.getBeanInfo(beanClass);
		return result;
	}

}
