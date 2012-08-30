package com.maestro.json.impl.beaninfo.factory.resolver;

import java.util.ArrayList;
import java.util.List;

import com.maestro.json.IBeanInfo;
import com.maestro.json.IBeanInfoContext;
import com.maestro.json.IBeanResolver;
import com.maestro.json.JsonException;

@SuppressWarnings("rawtypes")
public class CompositeBeanResolver extends AbstractBeanResolver {

	private List<IBeanResolver> resolvers;
	
	public CompositeBeanResolver() {
		init();
		resolvers.add(new PolymorphismBeanResolver());
		resolvers.add(new SimpleBeanResolver());
		initResolvers();
	}

	protected void init() {
		resolvers = new ArrayList<IBeanResolver>();
	}
	
	public CompositeBeanResolver(List<IBeanResolver> resolvers) {
		init();
		setResolvers(resolvers);
	}
	
	@Override
	public void setBeanContext(IBeanInfoContext context) {
		this.beanContext = context;
		for (IBeanResolver resolver : resolvers) {
			resolver.setBeanContext(context);
		}
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
		return result ;
	}

	public void setResolvers(List<IBeanResolver> resolvers) {
		this.resolvers = resolvers;
		initResolvers();
	}

	private void initResolvers() {
		setBeanContext(beanContext);
	}

}
