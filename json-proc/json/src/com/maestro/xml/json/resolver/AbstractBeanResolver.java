package com.maestro.xml.json.resolver;

import com.maestro.xml.IBeanContext;
import com.maestro.xml.IBeanResolver;

public abstract class AbstractBeanResolver implements IBeanResolver {

	protected IBeanContext beanContext;

	public IBeanContext getBeanContext() {
		return beanContext;
	}

	public void setBeanContext(IBeanContext beanContext) {
		this.beanContext = beanContext;
	}
}
