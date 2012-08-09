package com.maestro.xml.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.maestro.xml.IBeanAdapter;
import com.maestro.xml.IBeanInfo;
import com.maestro.xml.IBeanProcessor;
import com.maestro.xml.IBeanResolver;
import com.maestro.xml.IFieldResolver;
import com.maestro.xml.JsonException;
import com.maestro.xml.json.builder.JsonArray;
import com.maestro.xml.json.builder.JsonObject;
import com.maestro.xml.json.builder.JsonStringer;

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
	public String serialize(Object bean) throws JsonException {
		if (bean == null) {
			return null;
		}
		
		JsonStringer jsonWriter = new JsonStringer();
		String result = serialize(bean, jsonWriter, false);
		return result;
	}

	public String serialize(Object bean, JsonStringer jsonWriter, boolean stringValue) throws JsonException {
		if (bean == null) {
			return null;
		}
		if (stringValue) {
			// TODO try get value from cdata field
			jsonWriter.value(bean);
		} else {
			jsonWriter.object();
			Class beanClass = bean.getClass();
			IBeanInfo beanInfo = beanResolver.getBean(beanClass);

			for (Field field : beanInfo.getAttrs()) {
				processField(bean, jsonWriter, beanInfo, field);
			}
			jsonWriter.endObject();
		}
		return jsonWriter.toString();
	}

	private void processField(Object bean, JsonStringer jsonWriter,	IBeanInfo beanInfo, Field field) throws JsonException {
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
	public <T> T deserialize(Class<T> beanClass, String json) throws JsonException {
		JsonObject jsonObject = new JsonObject(json);
		T result = deserialize(beanClass, jsonObject);
		return result ;
	}

	@Override
	public <T> T deserialize(Class<T> beanClass, JsonObject jObject) throws JsonException {
		T result = deserialize(beanClass, jObject, false);
		return result ;
	}
	
	public <T> T deserialize(Class<T> beanClass, JsonObject jObject, boolean markedAsString) throws JsonException {
		IBeanInfo beanInfo = beanResolver.getBean(beanClass, jObject.toString());

		// Attributes processing
		T bean = JsonProcessorUtil.initializeBean(beanClass);
		if (bean != null) {
			for (Field field : beanInfo.getAttrs()) {
            	String fieldName = getFieldName(field);
            	Object value = jObject.get(fieldName);
            	
            	boolean primitive = fieldResolver.isPrimitive(value);
            	boolean stringField = beanInfo.isStringValue(field);

            	Class fieldType = field.getType();
            	if (stringField && !(value instanceof JsonArray)) {
            		IBeanInfo stringBeanInfo = beanResolver.getBean(fieldType);
            		Field fieldForStringValue = stringBeanInfo.getCdataField();
					value = injectStringField(fieldType, fieldForStringValue , value.toString());
            	} else if (primitive && !stringField) {
            		value = fieldResolver.convertToObject(fieldType, value);
            	} else if (value instanceof JsonObject) {
            		JsonObject jObj = (JsonObject) value;
            		value = deserialize(fieldType, jObj, stringField);
            	} else if (value instanceof JsonArray) {
            		JsonArray jArray = (JsonArray) value;
            		value = deserializeArray(field, jArray, stringField);
            	}
                adapter.setPropValue(field, bean, value);
			}
		}
		return bean;
	}

	protected <T> T injectStringField(Class<T> beanClass, Field field, String value) throws JsonException {
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
	
	protected void writeKey(JsonStringer jsonWriter, String fieldName) throws JsonException {
		// TODO decoding
		jsonWriter.key(fieldName);
	}
	
	
	protected void serializeArray(Object value, String fieldName, JsonStringer jsonWriter, boolean stringValue) throws JsonException {
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

	protected Object deserializeArray(Field attr, JsonArray jArray, boolean markedAsString) throws JsonException {
		Object result = null;
		Class fieldType = attr.getType();
		ArrayList listToFill = new ArrayList();
		Class genericFieldClass = (Class) JsonProcessorUtil.getGenericTypes(attr);
		for (Object item : jArray) {
			Object arrayObj = null;
			if (item instanceof JsonObject) {
				JsonObject jItem = (JsonObject) item;
				arrayObj = deserialize(genericFieldClass, jItem, markedAsString);
			} else if (item instanceof String) {
				String stringItem = (String)item;
				IBeanInfo beanInfo = beanResolver.getBean(genericFieldClass);;
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
