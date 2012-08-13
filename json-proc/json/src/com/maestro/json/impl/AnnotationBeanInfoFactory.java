package com.maestro.json.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.maestro.json.IBeanInfo;
import com.maestro.json.JsonElement;

public class AnnotationBeanInfoFactory extends AbstractBeanInfoFactory {

	@SuppressWarnings("unused")
	private List<String> beanInfoPackegeScan; 
	
	@Override
	protected void initializeContext() {
		// TODO implement package scaning
	}

	@Override
	protected IBeanInfo initializeBean(Class<?> beanClass) {
		Iterable<Field> fields = getFields(beanClass);

        Map<Field, JsonElement> attrs = new HashMap<Field, JsonElement>();
		for (Field field : fields) {
        	JsonElement annotation = field.getAnnotation(JsonElement.class);

            if (annotation != null) {
            	attrs .put(field, annotation);
            }
        }

		IBeanInfo result = new JsonBeanInfo(beanClass, attrs);
		return result;
	}
    
    public Collection<Field> getFields(Class<?> classDesc) {
		Set<Field> fields = new HashSet<Field>();
		fields.addAll(Arrays.asList(classDesc.getDeclaredFields()));
		
		if (classDesc.getSuperclass() != null) {
			fields.addAll(getFields(classDesc.getSuperclass()));
		}
		
		return fields;
	}


}