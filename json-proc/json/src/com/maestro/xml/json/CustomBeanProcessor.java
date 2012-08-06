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
	
	// TODO destroy XLog

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
		String result = serialize(bean, jsonWriter, false);
		return result;
	}

	public String serialize(Object bean, JSONStringer jsonWriter, boolean stringValue) {
		if (bean == null) {
			return null;
		}
		try {
			if (stringValue) {
				// TODO try get value from cdata field
				jsonWriter.value(bean);
			} else {
				jsonWriter.object();
				Class beanClass = bean.getClass();
				JBeanInfo beanInfo = beanResolver.getBean(beanClass);
	
				for (Field field : beanInfo.getAttrs()) {
					processField(bean, jsonWriter, beanInfo, field);
				}
				jsonWriter.endObject();
			}
		} catch (Exception e) {
			XLog.onError(e, "Can't process Bean");
		}
		return jsonWriter.toString();
	}

	private void processField(Object bean, JSONStringer jsonWriter,	JBeanInfo beanInfo, Field field) throws JSONException {
		Object value = adapter.getPropValue(field, bean);
		
		if (value != null) {
			boolean primitive = fieldResolver.isPrimitive(value);
			String fieldName = getFieldName(field);
			boolean stringValue = beanInfo.isStringValue(field);
			boolean array = value.getClass().isArray() || value instanceof Collection;
			if (!array) {
				writeKey(jsonWriter, fieldName);
			}
			if (primitive) {
				value = fieldResolver.convertFromObject(value);
				jsonWriter.value(value);
			} else if (array) {
				serializeArray(value, fieldName, jsonWriter, stringValue);
			} else {
				serialize(value, jsonWriter, stringValue);
			}
		}
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
		T result = deserialize(beanClass, jObject, false);
		return result ;
	}
	
	public <T> T deserialize(Class<T> beanClass, JSONObject jObject, boolean markedAsString) {
		JBeanInfo beanInfo = beanResolver.getBean(beanClass, jObject.toString());
		beanClass = beanInfo.getBeanClass();

		// Attributes processing
		T bean = JsonProcessorUtil.initializeBean(beanClass);
		if (bean != null) {
			for (Field field : beanInfo.getAttrs()) {
            	String fieldName = getFieldName(field);
            	Object value = jObject.get(fieldName);
            	
            	boolean primitive = fieldResolver.isPrimitive(value);
            	boolean stringField = beanInfo.isStringValue(field);

            	Class fieldType = field.getType();
            	if (stringField && !(value instanceof JSONArray)) {
            		JBeanInfo stringBeanInfo = beanResolver.getBean(fieldType);
            		Field fieldForStringValue = stringBeanInfo.getCdataField();
					value = injectStringField(fieldType, fieldForStringValue , value.toString());
            	} else if (primitive && !stringField) {
            		value = fieldResolver.convertToObject(fieldType, value);
            	} else if (value instanceof JSONObject) {
            		JSONObject jObj = (JSONObject) value;
            		value = deserialize(fieldType, jObj, stringField);
            	} else if (value instanceof JSONArray) {
            		JSONArray jArray = (JSONArray) value;
            		value = deserializeArray(field, jArray, stringField);
            	}
                adapter.setPropValue(field, bean, value);
			}
		}
		return bean;
	}

	protected <T> T injectStringField(Class<T> beanClass, Field field, String value) {
		if (field == null) {
			// TODO add throw exception
			return null;
		}
		T bean = JsonProcessorUtil.initializeBean(beanClass);
		adapter.setPropValue(field, bean, value);
		return bean;
	}
	
	protected String getFieldName(Field attr) {
		// TODO decoding
		String fieldName = JsonProcessorUtil.getFieldName(attr);
		return fieldName;
	}
	
	protected void writeKey(JSONStringer jsonWriter, String fieldName) throws JSONException {
		// TODO decoding
		jsonWriter.key(fieldName);
	}
	
	
	protected void serializeArray(Object value, String fieldName, JSONStringer jsonWriter, boolean stringValue) throws JSONException {
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
				serialize(item, jsonWriter, stringValue);
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

	protected Object deserializeArray(Field attr, JSONArray jArray, boolean markedAsString) {
		Object result = null;
		Class fieldType = attr.getType();
		ArrayList listToFill = new ArrayList();
		Class genericFieldClass = (Class) JsonProcessorUtil.getGenericTypes(attr);
		for (Object item : jArray) {
			Object arrayObj = null;
			if (item instanceof JSONObject) {
				JSONObject jItem = (JSONObject) item;
				arrayObj = deserialize(genericFieldClass, jItem, markedAsString);
			} else if (item instanceof String) {
				String stringItem = (String)item;
				JBeanInfo beanInfo = beanResolver.getBean(genericFieldClass);;
				Field stringField = beanInfo.getCdataField();
				arrayObj = injectStringField(genericFieldClass, stringField , stringItem);
			}
			listToFill.add(arrayObj);
			// TODO primitive types setting
		}
		boolean isArray = fieldType.isArray();
		if (isArray) {
			Object[] newInstance = (Object[]) Array.newInstance(genericFieldClass, jArray.length());
			result = listToFill.toArray(newInstance);
		} else {
			Collection coll = JsonProcessorUtil.initializeBean(fieldType);
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
