package com.maestro.json.impl;

import java.util.List;

import com.maestro.json.IBeanInfo;
import com.maestro.json.JsonException;

public class XmlBeanInfoFactory extends AbstractBeanInfoFactory {

	@SuppressWarnings("unused")
	private List<String> xmlMappingPathes;
	
	@Override
	protected void initializeContext() {
		// TODO implement xml context initialization
	}

	@Override
	protected IBeanInfo initializeBean(Class<?> beanClass) throws JsonException {
		throw new JsonException("Json bean info can't be found in the context.");
	}

	public void setXmlMappingPathes(List<String> xmlMappingPathes) {
		this.xmlMappingPathes = xmlMappingPathes;
	}

}
