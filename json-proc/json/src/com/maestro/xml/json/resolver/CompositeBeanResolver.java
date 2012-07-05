package com.maestro.xml.json.resolver;

import java.util.*;

import com.maestro.xml.IBeanResolver;
import com.maestro.xml.json.JBeanInfo;

@SuppressWarnings("rawtypes")
public class CompositeBeanResolver implements IBeanResolver {

	private List<IBeanResolver> resolvers;
	private Map<Class, JBeanInfo> beanInfos;
	
	public CompositeBeanResolver(List<IBeanResolver> resolvers) {
		beanInfos = new HashMap<Class, JBeanInfo>();
		setResolvers(resolvers);
	}
	
	@Override
	public JBeanInfo getBean(Class beanClass) {
		return getBean(beanClass, null);
	}

	@Override
	public JBeanInfo getBean(Class beanClass, String json) {
		JBeanInfo result = null;
		
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

	protected JBeanInfo addBeanInfo(Class beanClass) {
		JBeanInfo result = null;
		if (!beanInfos.containsKey(beanClass)) {
			result = new JBeanInfo(beanClass);
			beanInfos.put(beanClass, result);
		}
		return result;
	}

	@Override
	public void setInfos(Map<Class, JBeanInfo> infos) {
		this.beanInfos = infos;

	}

	public void setResolvers(List<IBeanResolver> resolvers) {
		this.resolvers = resolvers;
		for (IBeanResolver resolver : resolvers) {
			resolver.setInfos(beanInfos);
		}
	}

}
