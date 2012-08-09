package com.maestro.xml.json.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maestro.xml.IBeanInfo;
import com.maestro.xml.IBeanResolver;
import com.maestro.xml.JsonException;
import com.maestro.xml.json.JsonBeanInfo;

@SuppressWarnings("rawtypes")
public class CompositeBeanResolver implements IBeanResolver {

	private List<IBeanResolver> resolvers;
	private Map<Class, IBeanInfo> beanInfos;
	
	public CompositeBeanResolver(List<IBeanResolver> resolvers) {
		beanInfos = new HashMap<Class, IBeanInfo>();
		setResolvers(resolvers);
	}
	
	@Override
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
		if (!beanInfos.containsKey(beanClass)) {
			result = new JsonBeanInfo(beanClass);
			beanInfos.put(beanClass, result);
		}
		return result;
	}

	@Override
	public void setInfos(Map<Class, IBeanInfo> infos) {
		this.beanInfos = infos;

	}

	public void setResolvers(List<IBeanResolver> resolvers) {
		this.resolvers = resolvers;
		for (IBeanResolver resolver : resolvers) {
			resolver.setInfos(beanInfos);
		}
	}

}
