package com.maestro.xml.json.resolver;

import java.util.ArrayList;
import java.util.List;

import com.maestro.xml.IBeanContext;
import com.maestro.xml.IBeanInfo;
import com.maestro.xml.IBeanResolver;
import com.maestro.xml.JsonException;
import com.maestro.xml.json.DefaultBeanContext;
import com.maestro.xml.json.JsonBeanInfo;

@SuppressWarnings("rawtypes")
public class CompositeBeanResolver extends AbstractBeanResolver {

	private List<IBeanResolver> resolvers;
	private IBeanContext beanContext;
	
	public CompositeBeanResolver() {
		init();
		resolvers.add(new PolymorphismBeanResolver());
		resolvers.add(new SimpleBeanResolver());
		initResolvers();
	}

	protected void init() {
		resolvers = new ArrayList<IBeanResolver>();
		beanContext = new DefaultBeanContext();
	}
	
	public CompositeBeanResolver(List<IBeanResolver> resolvers) {
		init();
		setResolvers(resolvers);
	}
	
	public IBeanInfo getBean(Class beanClass) throws JsonException {
		return getBean(beanClass, null);
	}

	@Override
	public IBeanInfo getBean(Class beanClass, String json) throws JsonException {
		IBeanInfo result = null;
		
		for (IBeanResolver resolver : resolvers) {
			result = resolver.getBean(beanClass, json);
			
			if (result != null) {
				break;
			}
		}
		if (result == null) {
			result = addBeanInfo(beanClass);
		}
		
		return result ;
	}

	protected JsonBeanInfo addBeanInfo(Class beanClass) {
		JsonBeanInfo result = null;
		if (!beanContext.containsBeanInfo(beanClass)) {
			result = new JsonBeanInfo(beanClass);
			beanContext.addBeanInfo(result);
		}
		return result;
	}

	@Override
	public void setBeanContext(IBeanContext context) {
		this.beanContext = context;
	}

	public void setResolvers(List<IBeanResolver> resolvers) {
		this.resolvers = resolvers;
		initResolvers();
	}

	private void initResolvers() {
		for (IBeanResolver resolver : this.resolvers) {
			resolver.setBeanContext(beanContext);
		}
	}

}
