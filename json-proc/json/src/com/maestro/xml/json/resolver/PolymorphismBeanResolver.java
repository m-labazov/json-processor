package com.maestro.xml.json.resolver;

import java.lang.reflect.Field;
import java.util.*;

import com.maestro.xml.IBeanResolver;
import com.maestro.xml.json.*;
import com.maestro.xml.json.builder.JSONException;
import com.maestro.xml.json.builder.JSONObject;
import com.maestro.xml.xlog.XLog;

@SuppressWarnings("rawtypes")
public class PolymorphismBeanResolver implements IBeanResolver {

	private Map<Class, JBeanInfo> beanInfos;
	
	@Override
	public JBeanInfo getBean(Class beanClass) {
		return null;
	}

	@Override
	public JBeanInfo getBean(Class beanClass, String json) {
		if (json == null || json.isEmpty()) {
			return null;
		}
		
		List<JBeanInfo> relatedInfos = getRelatedBeanInfos(beanClass);
		JBeanInfo result = null;
		
		try {
			JSONObject obj = new JSONObject(json);
			
			String[] namesArray = JSONObject.getNames(obj);
			List<String> names = Arrays.asList(namesArray);
			
			int maxEqualedFields = 0;
			
			for (JBeanInfo info : relatedInfos) {
				int equaledFields = 0;
				
				Collection<Field> attrs = info.getAttrs();
				for (Field field : attrs) {
					String fieldName = JsonProcessorUtil.getFieldName(field);
					if (names.contains(fieldName)) {
						equaledFields++;
					}
				}
				
				if (equaledFields == names.size()) {
					if (names.size() == attrs.size()) {
						result = info;
						break;
					} else {
						maxEqualedFields = equaledFields;
						result = info;
					}
				} else if (equaledFields > maxEqualedFields) {
					result = info;
					maxEqualedFields = equaledFields;
				}
			}
			
		} catch (JSONException e) {
			XLog.onError(e, "Can't process Bean");
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	protected List<JBeanInfo> getRelatedBeanInfos(Class beanClass) {
		List<JBeanInfo> relatedInfos = new ArrayList<JBeanInfo>();
		
		for (Map.Entry<Class, JBeanInfo> entry : beanInfos.entrySet()) {
			if (beanClass.isAssignableFrom(entry.getKey())) {
				relatedInfos.add(entry.getValue());
			}
		}
		
		return relatedInfos;
	}

	@Override
	public void setInfos(Map<Class, JBeanInfo> infos) {
		this.beanInfos = infos;
	}

}
