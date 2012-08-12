package com.maestro.xml;

import java.lang.reflect.Field;
import java.util.Collection;

public interface IBeanInfo {

	Collection<Field> getAttrs();

	boolean isStringValue(Field field);

	Class<?> getBeanClass();

	Field getCdataField();

}
