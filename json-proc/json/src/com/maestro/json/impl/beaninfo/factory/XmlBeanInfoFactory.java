package com.maestro.json.impl.beaninfo.factory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.maestro.json.IBeanInfo;
import com.maestro.json.JsonException;
import com.maestro.json.impl.beaninfo.factory.xml.XmlBeanInfoDefinition;
import com.maestro.json.impl.beaninfo.factory.xml.XmlBeanInfoDefinitions;
import com.maestro.json.impl.beaninfo.factory.xml.XmlFieldDefinition;

public class XmlBeanInfoFactory extends AbstractBeanInfoFactory {

	private List<String> xmlMappingPathes;

	@Override
	protected void initializeContext() {
		for (String path : xmlMappingPathes) {
			try {
				JAXBContext context = JAXBContext.newInstance(
						XmlBeanInfoDefinition.class, XmlFieldDefinition.class,
						XmlBeanInfoDefinitions.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				XmlBeanInfoDefinitions result = (XmlBeanInfoDefinitions) unmarshaller
						.unmarshal(new File(path));
				processXml(result);
			} catch (JAXBException e) {
				// TODO add logging
			}
		}
	}

	private void processXml(XmlBeanInfoDefinitions result) {
		List<XmlBeanInfoDefinition> definitions = result.getDefinitions();
		for (XmlBeanInfoDefinition def : definitions) {
			String beanClassRef = def.getBeanClassRef();
			Class<?> beanClass = null;
			try {
				beanClass = Class.forName(beanClassRef);
			} catch (ClassNotFoundException e) {
				// TODO process exception
			}

			Map<Field, XmlFieldDefinition> attrs = new HashMap<Field, XmlFieldDefinition>();
			for (XmlFieldDefinition fieldDef : def.getFieldDefinitions()) {
				String fieldRef = fieldDef.getJavaField();
				try {
					Field field = beanClass.getField(fieldRef);
					attrs.put(field, fieldDef);
				} catch (Exception e) {
					// TODO process error
				}
			}
			if (!attrs.isEmpty()) {
				context.addBeanInfo(def);
			}
		}
	}

	@Override
	protected IBeanInfo initializeBean(Class<?> beanClass) throws JsonException {
		throw new JsonException("Json bean info can't be found in the context.");
	}

	public void setXmlMappingPathes(List<String> xmlMappingPathes) {
		this.xmlMappingPathes = xmlMappingPathes;
	}

}
