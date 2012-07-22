package com.maestro.xml.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.maestro.xml.xlog.XLog;

public class JSONProcessorUtil {

	private JSONProcessorUtil() {}
	
	public static Type getGenericTypes(Field attr) {
		Class<?> genericFieldClass = null;
		Class<?> fieldType = attr.getType();
		boolean isArray = fieldType.isArray();
		if (isArray) {
			genericFieldClass = fieldType.getComponentType();
		} else if (attr.getGenericType() != null) {
			ParameterizedType fieldParametrization = (ParameterizedType) attr.getGenericType();
			Type[] actualTypeArguments = fieldParametrization.getActualTypeArguments();
			if (actualTypeArguments.length != 0) {
				genericFieldClass = (Class<?>) actualTypeArguments[0];
			}
		}
		return genericFieldClass;
	}

    public static String getFieldName(Field field) {
    	String fieldName = field.getAnnotation(JDomElement.class).name();
		if (fieldName == null || fieldName.isEmpty()) {
			fieldName = field.getName();
		}
		return fieldName;
    }
    
    public static <T> T initializeBean(Class<T> fieldType) {
		T coll = null;
		if (!fieldType.isInterface() && !Modifier.isAbstract(fieldType.getModifiers())) {
			try {
				coll = (T) fieldType.newInstance();
			} catch (Exception e) {
				XLog.onError(e, "Can't process attributes for JSONObject");
			}
		}
		return coll;
	}
	
}
