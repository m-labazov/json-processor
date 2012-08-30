package com.maestro.json.impl.beaninfo.factory.resolver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.maestro.json.IBeanInfo;
import com.maestro.json.JsonException;
import com.maestro.json.impl.JsonProcessorUtil;
import com.maestro.json.impl.beaninfo.JsonBeanInfo;
import com.maestro.json.impl.builder.JsonObject;

@SuppressWarnings("rawtypes")
public class PolymorphismBeanResolver extends AbstractBeanResolver {

	/**
	 * return null, because if we throw exception composite resolver will be broken.
	 */
	@Override
	public JsonBeanInfo getBean(Class beanClass) throws JsonException {
		return null;
	}

	@Override
	public IBeanInfo getBean(Class beanClass, String json) throws JsonException {
		if (json == null || json.isEmpty()) {
			// don't throw exception, because composite resolver will be broken.			
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
		
		for (IBeanInfo info : beanContext.getBeanInfos()) {
			if (beanClass.isAssignableFrom(info.getBeanClass())) {
				relatedInfos.add(info);
			}
		}
		
		return relatedInfos;
	}

}
