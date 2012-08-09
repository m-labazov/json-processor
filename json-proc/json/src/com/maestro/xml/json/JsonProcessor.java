package com.maestro.xml.json;

import com.maestro.xml.IBeanProcessor;
import com.maestro.xml.JsonException;
import com.maestro.xml.json.builder.JsonObject;


public class JsonProcessor {
    
    private IBeanProcessor beanProcessor;

    /**
     * Constructor
     */
    public JsonProcessor(IBeanProcessor beanProcessor) {
    	this.beanProcessor = beanProcessor;
    }


    public String processBean(Object bean) throws JsonException {
    	String result = beanProcessor.serialize(bean);
    	return result;
    }

    public <T> T processJson(Class<T> bean, String json) throws JsonException {
    	T result = beanProcessor.deserialize(bean, json);
    	return result;
    }

    public <T> T processJson(Class<T> bean, JsonObject jObject) throws JsonException {
    	T result = beanProcessor.deserialize(bean, jObject);
    	return result;
    }

}
