package com.maestro.xml.json.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.maestro.xml.IBeanInfo;
import com.maestro.xml.IBeanResolver;
import com.maestro.xml.JsonException;
import com.maestro.xml.json.JsonBeanInfo;
import com.maestro.xml.json.JsonProcessorUtil;
import com.maestro.xml.json.builder.JsonObject;

@SuppressWarnings("rawtypes")
public class PolymorphismBeanResolver implements IBeanResolver {

	private Map<Class, IBeanInfo> beanInfos;
	
	@Override
	public JsonBeanInfo getBean(Class beanClass) {
		return null;
	}

	@Override
	public IBeanInfo getBean(Class beanClass, String json) throws JsonException {
		if (json == null || json.isEmpty()) {
			return null;
		}
		
		List<IBeanInfo> relatedInfos = getRelatedBeanInfos(beanClass);
		IBeanInfo result = null;
		
		JsonObject obj = new JsonObject(json);
		
		String[] namesArray = JsonObject.getNames(obj);
		List<String> names = Arrays.asList(namesArray);
		
		int maxEqualedFields = 0;
		
		for (IBeanInfo info : relatedInfos) {
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
		return result;
	}

	@SuppressWarnings("unchecked")
	protected List<IBeanInfo> getRelatedBeanInfos(Class beanClass) {
		List<IBeanInfo> relatedInfos = new ArrayList<IBeanInfo>();
		
		for (Map.Entry<Class, IBeanInfo> entry : beanInfos.entrySet()) {
			if (beanClass.isAssignableFrom(entry.getKey())) {
				relatedInfos.add(entry.getValue());
			}
		}
		
		return relatedInfos;
	}

	@Override
	public void setInfos(Map<Class, IBeanInfo> infos) {
		this.beanInfos = infos;
	}

}
