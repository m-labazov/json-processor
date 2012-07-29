package com.maestro.xml.json;

import com.maestro.xml.IBeanProcessor;
import com.maestro.xml.json.builder.JSONObject;


public class JsonProcessor {
    
    private IBeanProcessor beanProcessor;

    /**
     * Constructor
     */
    public JsonProcessor(IBeanProcessor beanProcessor) {
    	this.beanProcessor = beanProcessor;
    }


    public String processBean(Object bean) {
    	String result = beanProcessor.serialize(bean);
    	return result;
    }

    public <T> T processJson(Class<T> bean, String json) {
    	T result = beanProcessor.deserialize(bean, json);
    	return result;
    }

    public <T> T processJson(Class<T> bean, JSONObject jObject) {
    	T result = beanProcessor.deserialize(bean, jObject);
    	return result;
    }

}
