package com.maestro.json.impl.beaninfo;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.maestro.json.IBeanInfo;
import com.maestro.json.JsonElement;

public class JsonBeanInfo implements IBeanInfo {

	private Class<?> beanClass;
	private Map<Field, JsonElement> attrs = new HashMap<Field, JsonElement>();

	public JsonBeanInfo(Class<?> beanClass, Map<Field, JsonElement> attrs) {
		this.beanClass = beanClass;
		this.attrs = attrs;
	}

	@Override
	public Collection<Field> getAttrs() {
		return attrs.keySet();
	}

	@Override
	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public boolean isStringValue(Field field) {
		JsonElement beanAnnotation = attrs.get(field);
		boolean result = beanAnnotation.stringValue();
		return result;
	}

	@Override
	public Field getCdataField() {
		Field result = null;
		for (Map.Entry<Field, JsonElement> entry : attrs.entrySet()) {
			JsonElement beanAnnotation = entry.getValue();
			boolean cdata = beanAnnotation.cdataField();
			if (cdata) {
				result = entry.getKey();
				break;
			}
		}
		return result;
	}

}
