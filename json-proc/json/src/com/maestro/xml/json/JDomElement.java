package com.maestro.xml.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking fields of the class, which will be inserted into the JSON object
 * 
 * @author Maksym Labazov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface JDomElement {
	/**
	 * Defines if the field, marked with this annotation, is written in JSON as a string
	 * @return
	 */
    boolean stringValue() default false;
    /**
     * Defines default name of the JSON element
     * @return
     */
    String name() default "";
    /**
     * Defines the field, where to write string value from the JSON object
     * @return
     */
    boolean cdataField() default false;

}
