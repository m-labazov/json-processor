package com.maestro.json.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.maestro.json.IBeanAdapter;
import com.maestro.json.IBeanInfo;
import com.maestro.json.IBeanInfoFactory;
import com.maestro.json.IBeanProcessor;
import com.maestro.json.IFieldResolver;
import com.maestro.json.JsonException;
import com.maestro.json.impl.builder.JsonArray;
import com.maestro.json.impl.builder.JsonObject;
import com.maestro.json.impl.builder.JsonStringer;
import com.maestro.json.impl.resolver.CompositeFieldResolver;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomBeanProcessor implements IBeanProcessor {
	
	private IBeanInfoFactory beanFactory;
	private IBeanAdapter adapter;
	private IFieldResolver fieldResolver;
	
	public CustomBeanProcessor() {
		IFieldResolver fieldRslover = new CompositeFieldResolver();
		IBeanAdapter adapter = XBeanAdapter.getInstance();
		init(null, adapter, fieldRslover);
	}
	
	public CustomBeanProcessor(IBeanInfoFactory beanFactory, IBeanAdapter adapter, IFieldResolver fieldResolver) {
		init(beanFactory, adapter, fieldResolver);
	}

	protected void init(IBeanInfoFactory beanFactory, IBeanAdapter adapter, IFieldResolver fieldResolver) {
		this.beanFactory = beanFactory;
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
			IBeanInfo beanInfo = beanFactory.getBean(beanClass);

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
		IBeanInfo beanInfo = beanFactory.getBean(beanClass, jObject.toString());

		// Attributes processing
		T bean = (T) JsonProcessorUtil.initializeBean(beanInfo.getBeanClass());
		if (bean != null) {
			for (Field field : beanInfo.getAttrs()) {
            	String fieldName = getFieldName(field);
            	Object value = jObject.get(fieldName);
            	
            	boolean primitive = fieldResolver.isPrimitive(value);
            	boolean stringField = beanInfo.isStringValue(field);

            	Class fieldType = field.getType();
            	if (stringField && !(value instanceof JsonArray)) {
            		IBeanInfo stringBeanInfo = beanFactory.getBean(fieldType);
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

	/**
	 * Method create bean and insert string value into its default cdata field
	 * @param <T> type of the bean
	 * @param beanClass
	 * @param field
	 * @param value
	 * @return bean with filled cdata field
	 * @throws JsonException if there is no cdata field in the bean
	 */
	protected <T> T injectStringField(Class<T> beanClass, Field field, String value) throws JsonException {
		if (field == null) {
			throw new JsonException("Can't insert string value, because bean doesn't have cdata field.");
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
				IBeanInfo beanInfo = beanFactory.getBean(genericFieldClass);;
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

	public void setBeanFactory(IBeanInfoFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}
