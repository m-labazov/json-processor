package com.maestro.xml.json.resolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.maestro.xml.IFieldResolver;

@SuppressWarnings("rawtypes")
public class SimpleDateFieldResolver implements IFieldResolver {

	private static final String CUSTOM_DATE_FORMAT = "dd.MM.yyyy";
	private static final String CUSTOM_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
	private String pattern;
	
	public SimpleDateFieldResolver() {}
	
	public SimpleDateFieldResolver(String pattern) {
		this.pattern = pattern;
	}
	
	
	@Override
	public Object convertToObject(Class beanClass, Object value) {
		Object result = getDate(value);
		return result;
	}

	@Override
	public boolean isPrimitive(Object value) {
		boolean result = false;
		
		if (value == null) {
			return result;
		} else if (value instanceof Date) {
			result = true;
		} else {
			Object resultDate = getDate(value);
			if (resultDate != null) {
				result = true;
			}
		}
		return result;
	}

	private Date getDate(Object value) {
		Date result = null;
		String textValue = value.toString();
		SimpleDateFormat sdf = getDateFormat();
		try {
			if (pattern != null && pattern.length() == textValue.length()) {
				sdf.applyPattern(pattern);
			} else if (CUSTOM_DATE_FORMAT.length() == textValue.length()) {
				sdf.applyPattern(CUSTOM_DATE_FORMAT);
			} else if (CUSTOM_DATE_TIME_FORMAT.length() == textValue.length()) {
				sdf.applyPattern(CUSTOM_DATE_TIME_FORMAT);
			}
			result = sdf.parse(textValue);
		} catch (ParseException ex) {}
		return result;
	}

	private SimpleDateFormat getDateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//		simpleDateFormat.toLocalizedPattern();
		return simpleDateFormat;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String convertFromObject(Object value) {
		String result = null;
		if (value == null) {
			return result;
		} else if (value instanceof Date) {
			Date dateValue = (Date) value;
			SimpleDateFormat sdf = getDateFormat();
			if (pattern != null && !pattern.isEmpty()) {
				sdf.applyPattern(pattern);
			} else {
				sdf.applyPattern(CUSTOM_DATE_FORMAT);
			}
			result = sdf.format(dateValue);
		}
		return result;
	}

}
