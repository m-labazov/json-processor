package com.maestro.xml.json;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class JBeanInfo {

    private String name;
    private Class beanClass;
    private Map<Field, JDomElement> attrs = new HashMap<Field, JDomElement>();

    public JBeanInfo() {
    }

    public JBeanInfo(Class classDesc) {
        initBean(classDesc);
    }

    public void initBean(Class classDesc) {
        Iterable<Field> fields = getFields(classDesc);

        for (Field field : fields) {
        	JDomElement annotation = field.getAnnotation(JDomElement.class);

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

    public Collection<Field> getAttrs() {
        return attrs.keySet();
    }

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

	public boolean isStringValue(Field field) {
		JDomElement beanAnnotation = attrs.get(field);
		boolean result = beanAnnotation.stringValue();
		return result;
	}

	public Field getCdataField() {
		Field result = null;
		for (Map.Entry<Field, JDomElement> entry : attrs.entrySet()) {
			JDomElement beanAnnotation = entry.getValue();
			boolean cdata = beanAnnotation.cdataField();
			if (cdata) {
				result = entry.getKey();
				break;
			}
		}
		return result;
	}

}
