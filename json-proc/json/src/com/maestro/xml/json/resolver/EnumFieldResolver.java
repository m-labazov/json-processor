package com.maestro.xml.json.resolver;

import com.maestro.xml.IFieldResolver;

@SuppressWarnings("rawtypes")
public class EnumFieldResolver implements IFieldResolver {

	@Override
	@SuppressWarnings("unchecked")
	public Object convertToObject(Class beanClass, Object value) {
		Object result = null;
		
		if (beanClass.isEnum()) {
			result = Enum.valueOf(beanClass, value.toString());
		}
		return result;
	}

	@Override
	public String convertFromObject(Object value) {
		String result = null;
		
		boolean primitive = isPrimitive(value);
		if (primitive) {
			Enum en = (Enum) value;
			result = en.name();
		}
		return result;
	}

	@Override
	public boolean isPrimitive(Object value) {
		boolean result = value.getClass().isEnum();
		return result;
	}

}
