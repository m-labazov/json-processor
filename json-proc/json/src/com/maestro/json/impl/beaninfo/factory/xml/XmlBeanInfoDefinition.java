package com.maestro.json.impl.beaninfo.factory.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.maestro.json.IBeanInfo;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlBeanInfoDefinition implements IBeanInfo {

	@XmlTransient
	private Class<?> beanClass;
	@XmlTransient
	private Map<Field, XmlFieldDefinition> attrs = new HashMap<Field, XmlFieldDefinition>();
	@XmlAttribute(required = true, name = "beanClass")
	private String beanClassRef;
	@XmlElement(name = "field")
	private List<XmlFieldDefinition> fieldDefinitions = new ArrayList<XmlFieldDefinition>();

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClassRef(String beanClassRef) {
		this.beanClassRef = beanClassRef;
	}

	public List<XmlFieldDefinition> getFieldDefinitions() {
		return fieldDefinitions;
	}

	public void setFieldDefinitions(List<XmlFieldDefinition> fieldDefinitions) {
		this.fieldDefinitions = fieldDefinitions;
	}

	@Override
	public Collection<Field> getAttrs() {
		return attrs.keySet();
	}

	@Override
	public boolean isStringValue(Field field) {
		boolean result = false;
		XmlFieldDefinition xmlFieldDefinition = attrs.get(field);
		if (xmlFieldDefinition != null) {
			result = xmlFieldDefinition.isStringValue();
		}
		return result;
	}

	@Override
	public Field getCdataField() {
		Field result = null;
		for (Entry<Field, XmlFieldDefinition> def : attrs.entrySet()) {
			if (def.getValue().isPcData()) {
				result = def.getKey();
				break;
			}
		}
		return result;
	}

	public String getBeanClassRef() {
		return beanClassRef;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public void setAttrs(Map<Field, XmlFieldDefinition> attrs) {
		this.attrs = attrs;
	}

}
