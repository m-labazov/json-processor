package com.maestro.xml.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.maestro.xml.IBeanAdapter;
import com.maestro.xml.IBeanProcessor;
import com.maestro.xml.IBeanResolver;
import com.maestro.xml.IFieldResolver;
import com.maestro.xml.json.builder.JSONArray;
import com.maestro.xml.json.builder.JSONException;
import com.maestro.xml.json.builder.JSONObject;
import com.maestro.xml.json.builder.JSONStringer;
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
			JBeanInfo beanInfo = beanResolver.getBean(beanClass);

			for (Field field : beanInfo.getAttrs()) {
				Object value = adapter.getPropValue(field, bean);
				
				if (value != null) {
					boolean stringValue = beanInfo.isStringValue(field);
					boolean primitive = fieldResolver.isPrimitive(value);
					String fieldName = getFieldName(field);
					if (stringValue) {
						writeKey(jsonWriter, fieldName);
						if (value.getClass().isArray() || value instanceof Collection) {
							Collection values = getArrayAsCollection(value);
							for (Object arrayValue : values) {
								jsonWriter.value(arrayValue);
							}
						} else {
							jsonWriter.value(value);
						}
					} else if (primitive) {
						writeKey(jsonWriter, fieldName);
						value = fieldResolver.convertFromObject(value);
						jsonWriter.value(value);
					} else if (value.getClass().isArray() || value instanceof Collection) {
						serializeArray(value, fieldName, jsonWriter);
					} else {
						writeKey(jsonWriter, fieldName);
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
		JBeanInfo beanInfo = beanResolver.getBean(beanClass, jObject.toString());
		beanClass = beanInfo.getBeanClass();

		// Attributes processing
		T bean = JSONProcessorUtil.initializeBean(beanClass);
		if (bean != null) {
			for (Field field : beanInfo.getAttrs()) {
            	String fieldName = getFieldName(field);
            	Object value = jObject.get(fieldName);
            	
            	boolean primitive = fieldResolver.isPrimitive(value);

            	Class fieldType = field.getType();
				if (primitive) {
            		value = fieldResolver.convertToObject(fieldType, value);
            	} else if (value instanceof JSONObject) {
            		JSONObject jObj = (JSONObject) value;
            		value = deserialize(fieldType, jObj);
            	} else if (value instanceof JSONArray) {
            		JSONArray jArray = (JSONArray) value;
            		value = deserializeArray(field, jArray);
            	}
                adapter.setPropValue(field, bean , value);
			}
		}
		return bean;
	}

	protected String getFieldName(Field attr) {
		// TODO decoding
		String fieldName = JSONProcessorUtil.getFieldName(attr);
		return fieldName;
	}
	
	protected void writeKey(JSONStringer jsonWriter, String fieldName) throws JSONException {
		// TODO decoding
		jsonWriter.key(fieldName);
	}
	
	
	protected void serializeArray(Object value, String fieldName, JSONStringer jsonWriter) throws JSONException {
		Collection items = getArrayAsCollection(value);
		if (items.isEmpty()) {
			return;
		}
		writeKey(jsonWriter, fieldName);
		jsonWriter.array();
		for (Object item : items) {
			boolean itemPrimitive = fieldResolver.isPrimitive(item);
			if (itemPrimitive) {
				jsonWriter.value(item.toString());
			} else {
				serialize(item, jsonWriter);
			}
		}
		jsonWriter.endArray();
	}

	protected Collection getArrayAsCollection(Object value) {
		Collection items = null;
		if (value.getClass().isArray()) {
			Object[] arrayValue = (Object[]) value;
			items = Arrays.asList(arrayValue);
		} else if (value instanceof Collection) {
			items = (Collection) value;
		}
		return items;
	}

	protected Object deserializeArray(Field attr, JSONArray jArray) {
		Object result = null;
		Class fieldType = attr.getType();
		ArrayList listToFill = new ArrayList();
		Class genericFieldClass = (Class) JSONProcessorUtil.getGenericTypes(attr);
		for (Object item : jArray) {
			if (item instanceof JSONObject) {
				JSONObject jItem = (JSONObject) item;
				Object arrayObj = deserialize(genericFieldClass, jItem);
				listToFill.add(arrayObj);
			}
			// TODO primitive types setting
		}
		boolean isArray = fieldType.isArray();
		if (isArray) {
			Object[] newInstance = (Object[]) Array.newInstance(genericFieldClass, jArray.length());
			result = listToFill.toArray(newInstance);
		} else {
			Collection coll = JSONProcessorUtil.initializeBean(fieldType);
			// TODO need to improve (if it's not standard collection interface)
			if (coll == null) {
				if (List.class.isAssignableFrom(fieldType)) {
					coll = new ArrayList();
				} else {
					coll = new HashSet();
				}
			}
			coll.addAll(listToFill);
			result = coll;
		}
		return result;
	}

}
