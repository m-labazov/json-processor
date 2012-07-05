package com.maestro.xml.json;

import java.lang.reflect.*;
import java.util.*;

import com.maestro.xml.*;
import com.maestro.xml.xlog.XLog;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomBeanProcessor implements IBeanProcessor {

	private IBeanResolver beanResolver;
	private IBeanAdapter adapter;
	private IFieldResolver fieldResolver;
	
	public CustomBeanProcessor(IBeanResolver beanResolver, IBeanAdapter adapter, IFieldResolver fieldResolver) {
		this.beanResolver = beanResolver;
		this.adapter = adapter;
		this.fieldResolver = fieldResolver;
	}
	
	@Override
	public String serialize(Object bean) {
		if (bean == null) {
			return null;
		}
		
		JSONStringer jsonWriter = new JSONStringer();
		String result = serialize(bean, jsonWriter);
		return result;
	}

	public String serialize(Object bean, JSONStringer jsonWriter) {
		if (bean == null) {
			return null;
		}
		try {
			
			jsonWriter.object();
			Class beanClass = bean.getClass();
			JBeanInfo beanInfo = getBeanInfo(beanClass);

			for (Field attr : beanInfo.getAttrs()) {
				Object value = adapter.getPropValue(attr, bean);
				
				if (value != null) {
					boolean primitive = isPrimitive(value);
					
					String fieldName = JBeanInfo.getFieldName(attr);
					if (primitive) {
						jsonWriter.key(fieldName);
						jsonWriter.value(value);
					} else if (value.getClass().isArray() || value instanceof Collection) {
						Collection items = null;
						if (value.getClass().isArray()) {
							items = Arrays.asList(value);
						} else if (value instanceof Collection) {
							items = (Collection) value;
						}
						
						if (items.isEmpty()) {
							continue;
						}
						
						jsonWriter.key(fieldName);
						jsonWriter.array();
						for (Object item : items) {
							boolean itemPrimitive = isPrimitive(item);
							if (itemPrimitive) {
								jsonWriter.value(item.toString());
							} else {
								serialize(item, jsonWriter);
							}
						}
						jsonWriter.endArray();
					} else {
						jsonWriter.key(fieldName);
						serialize(value, jsonWriter);
					}
				}
			}

			jsonWriter.endObject();
		} catch (Exception e) {
			XLog.onError(e, "Can't process Bean");
		}
		return jsonWriter.toString();
	}

	@Override
	public <T> T deserialize(Class<T> beanClass, String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			T result = deserialize(beanClass, jsonObject);
			return result ;
		} catch (JSONException e) {
			XLog.onError(e, "Can't process attributes for JSONObject");
		}
		return null;
	}

	@Override
	public <T> T deserialize(Class<T> beanClass, JSONObject jObject) {
		JBeanInfo beanInfo = getBeanInfo(beanClass, jObject.toString());

		// Attributes processing
		try {
			T bean = (T) beanInfo.getBeanClass().newInstance();
			for (Field attr : beanInfo.getAttrs()) {
            	String fieldName = JBeanInfo.getFieldName(attr);
            	Object value = jObject.get(fieldName);
            	
            	boolean primitive = isPrimitive(value);

            	Class fieldType = attr.getType();
				if (primitive) {
            		value = fieldResolver.convert(fieldType, value);
            	} else if (value instanceof JSONObject) {
            		JSONObject jObj = (JSONObject) value;
            		value = deserialize(fieldType, jObj);
            	} else if (value instanceof JSONArray) {
            		// TODO improve collection/array processing
            		Collection coll = null;
            		if (List.class.isAssignableFrom(fieldType)) {
            			coll = new ArrayList();
            		} else {
            			coll = new HashSet();
            		}
            		JSONArray jArray = (JSONArray) value;
            		for (Object item : jArray) {
            			if (item instanceof JSONObject) {
            				JSONObject jItem = (JSONObject) item;
            				Class genericFieldClass = Object.class;
            				if (attr.getGenericType() != null) {
	            				ParameterizedType fieldParametrization = (ParameterizedType) attr.getGenericType();
								Type[] actualTypeArguments = fieldParametrization.getActualTypeArguments();
								if (actualTypeArguments.length != 0) {
									genericFieldClass = (Class) actualTypeArguments[0];
								}
            				}
            				Object arrayObj = deserialize(genericFieldClass, jItem);
            				coll.add(arrayObj);
            			}
            		}
            		value = coll;
            	}
                adapter.setPropFValue(attr, bean , value);
			}
			return bean;
		} catch (Exception e) {
			XLog.onError(e, "Can't process attributes for JSONObject");
		}

		return null;
	}

	private boolean isPrimitive(Object value) {
		boolean result = false;
		
		if (value == null) {
			return result;
		}
		
		Class<? extends Object> beanClass = value.getClass();
		// TODO date and enum processing
		if (beanClass.isPrimitive() || value instanceof String || value instanceof Number) {
			result = true;
		}
		
		return result ;
	}

	public JBeanInfo getBeanInfo(Class beanClass) {
		JBeanInfo result = getBeanInfo(beanClass, null);
		return result;
	}
	
	public JBeanInfo getBeanInfo(Class beanClass, String json) {
		JBeanInfo beanInfo = beanResolver.getBean(beanClass, json);
		return beanInfo;
	}

}
