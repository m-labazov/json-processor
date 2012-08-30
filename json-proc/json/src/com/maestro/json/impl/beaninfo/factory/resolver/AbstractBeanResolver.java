package com.maestro.json.impl.beaninfo.factory.resolver;

import com.maestro.json.IBeanInfoContext;
import com.maestro.json.IBeanResolver;

public abstract class AbstractBeanResolver implements IBeanResolver {

	protected IBeanInfoContext beanContext;

	public IBeanInfoContext getBeanContext() {
		return beanContext;
	}

	public void setBeanContext(IBeanInfoContext beanContext) {
		this.beanContext = beanContext;
	}
}
