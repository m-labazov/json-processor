package com.maestro.xml.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("rawtypes")
public class JBeanInfo {

    private String name;
    private Class beanClass;
    private List<Field> attrs = new ArrayList<Field>();

    public JBeanInfo() {
    }

    public JBeanInfo(Class classDesc) {
        initBean(classDesc);
    }

    public void initBean(Class classDesc) {
        Iterable<Field> fields = getFields(classDesc);

        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(JDomElement.class);

            if (annotation != null) {
            	attrs.add(field);
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

    public List<Field> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Field> attrs) {
        this.attrs = attrs;
    }

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

}
