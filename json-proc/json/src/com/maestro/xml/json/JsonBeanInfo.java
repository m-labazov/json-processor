package com.maestro.xml.json;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.maestro.xml.IBeanInfo;

@SuppressWarnings("rawtypes")
public class JsonBeanInfo implements IBeanInfo {

    private String name;
    private Class beanClass;
    private Map<Field, JsonElement> attrs = new HashMap<Field, JsonElement>();

    public JsonBeanInfo() {
    }

    public JsonBeanInfo(Class classDesc) {
        initBean(classDesc);
    }

    public void initBean(Class classDesc) {
        Iterable<Field> fields = getFields(classDesc);

        for (Field field : fields) {
        	JsonElement annotation = field.getAnnotation(JsonElement.class);

            if (annotation != null) {
            	attrs.put(field, annotation);
            }
        }
        beanClass = classDesc;
    }

	public Collection<Field> getFields(Class classDesc) {
		Set<Field> fields = new HashSet<Field>();
		fields.addAll(Arrays.asList(classDesc.getDeclaredFields()));
		
		if (classDesc.getSuperclass() != null) {
			fields.addAll(getFields(classDesc.getSuperclass()));
		}
		
		return fields;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<Field> getAttrs() {
        return attrs.keySet();
    }

    @Override
	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
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
