package com.maestro.json.impl.beaninfo.factory;

import com.maestro.json.IBeanInfo;
import com.maestro.json.IBeanInfoContext;
import com.maestro.json.IBeanInfoFactory;
import com.maestro.json.IBeanResolver;
import com.maestro.json.JsonException;
import com.maestro.json.impl.beaninfo.DefaultBeanContext;
import com.maestro.json.impl.beaninfo.factory.resolver.CompositeBeanResolver;

public abstract class AbstractBeanInfoFactory implements IBeanInfoFactory {

	protected IBeanInfoContext context;
	protected IBeanResolver resolver;

	public AbstractBeanInfoFactory() {
		context = new DefaultBeanContext();
		resolver = new CompositeBeanResolver();
		resolver.setBeanContext(context);
	}

	protected abstract void initializeContext() throws Exception;

	protected abstract IBeanInfo initializeBean(Class<?> beanClass)
			throws JsonException;

	@Override
	public IBeanInfo getBean(Class<?> beanClass) throws JsonException {
		IBeanInfo result = getBean(beanClass, null);
		return result;
	}

	@Override
	public IBeanInfo getBean(Class<?> beanClass, String json)
			throws JsonException {
		IBeanInfo result = resolver.getBean(beanClass, json);
		if (result == null) {
			result = initializeBean(beanClass);
			context.addBeanInfo(result);
		}
		return result;
	}

}
