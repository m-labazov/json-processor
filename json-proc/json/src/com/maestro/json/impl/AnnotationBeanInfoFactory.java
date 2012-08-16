package com.maestro.json.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

	public AnnotationBeanInfoFactory() throws Exception {}
	
	public AnnotationBeanInfoFactory(String beanInfoPackegeScan) throws Exception {
		setBeanInfoPackegeScan(beanInfoPackegeScan);
		initializeContext();
	}

	private List<String> beanInfoPackegeScan; 
	
	@Override
	protected void initializeContext() throws Exception {
		if (beanInfoPackegeScan == null || beanInfoPackegeScan.isEmpty()) {
			return;
		}
		
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for (String beanPackage : beanInfoPackegeScan) {
			classes.addAll(ClassScanUtil.getClasses(beanPackage));
		}
		for (Class<?> clazz : classes) {
			IBeanInfo initializedBean = initializeBean(clazz);
			if (initializedBean != null) {
				context.addBeanInfo(initializedBean);
			}
		}
	}
	
	@Override
	protected IBeanInfo initializeBean(Class<?> beanClass) {
		Iterable<Field> fields = getFields(beanClass);

        Map<Field, JsonElement> attrs = new HashMap<Field, JsonElement>();
		for (Field field : fields) {
        	JsonElement annotation = field.getAnnotation(JsonElement.class);

            if (annotation != null) {
            	attrs.put(field, annotation);
            }
        }

		if (!attrs.isEmpty()) {
			IBeanInfo result = new JsonBeanInfo(beanClass, attrs);
			return result;
		} else {
			return null;
		}
	}
    
    public Collection<Field> getFields(Class<?> classDesc) {
		Set<Field> fields = new HashSet<Field>();
		fields.addAll(Arrays.asList(classDesc.getDeclaredFields()));
		
		if (classDesc.getSuperclass() != null) {
			fields.addAll(getFields(classDesc.getSuperclass()));
		}
		
		return fields;
	}

	public List<String> getBeanInfoPackegeScan() {
		return beanInfoPackegeScan;
	}

	public void setBeanInfoPackegeScan(List<String> beanInfoPackegeScan) {
		this.beanInfoPackegeScan = beanInfoPackegeScan;
	}

	public void setBeanInfoPackegeScan(String beanInfoPackegeScan) {
		ArrayList<String> packages = new ArrayList<String>();
		packages.add(beanInfoPackegeScan);
		setBeanInfoPackegeScan(packages);
	}
}
