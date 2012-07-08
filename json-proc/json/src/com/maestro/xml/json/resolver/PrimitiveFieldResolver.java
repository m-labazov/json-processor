package com.maestro.xml.json.resolver;

import com.maestro.xml.IFieldResolver;

@SuppressWarnings("rawtypes")
public class PrimitiveFieldResolver implements IFieldResolver {

	@Override
	public Object convertToObject(Class beanClass, Object value) {
		Object result = null;
		
		if (beanClass.equals(String.class)) {
			result = value;
		} else if (beanClass.equals(int.class) || beanClass.equals(Integer.class)) {
			result = Integer.parseInt(value.toString());
		} else if (beanClass.equals(byte.class) || beanClass.equals(Byte.class)) {
			result = Byte.parseByte(value.toString());
		} else if (beanClass.equals(short.class) || beanClass.equals(Short.class)) {
			result = Short.parseShort(value.toString());
		} else if (beanClass.equals(long.class) || beanClass.equals(Long.class)) {
			result = Long.parseLong(value.toString());
		} else if (beanClass.equals(double.class) || beanClass.equals(Double.class)) {
			result = Double.parseDouble(value.toString());
		} else if (beanClass.equals(float.class) || beanClass.equals(Float.class)) {
			result = Float.parseFloat(value.toString());
		}
		
		return result;
	}

	@Override
	public boolean isPrimitive(Object value) {
		boolean result = false;
		if (value == null) {
			return result ;
		}
		
		Class<? extends Object> beanClass = value.getClass();
		if (beanClass.isPrimitive() || value instanceof String || value instanceof Number) {
			result = true;
		}
		
		return result;
	}

	@Override
	public String convertFromObject(Object value) {
		String result = null;
		if (value == null) {
			return result ;
		}
		boolean primitive = isPrimitive(value);
		if (primitive) {
			result = value.toString();
		}
		return result;
	}

}
