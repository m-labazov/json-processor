package com.maestro.json.impl.beaninfo.factory.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFieldDefinition {

	@XmlAttribute(required = true, name = "field")
	private String javaField;
	@XmlAttribute(required = true, name = "attribute")
	private String xmlAttr;
	@XmlAttribute
	private boolean pcData;
	@XmlAttribute
	private boolean stringValue;

	public String getJavaField() {
		return javaField;
	}

	public void setJavaField(String javaField) {
		this.javaField = javaField;
	}

	public String getXmlAttr() {
		return xmlAttr;
	}

	public void setXmlAttr(String xmlAttr) {
		this.xmlAttr = xmlAttr;
	}

	public boolean isPcData() {
		return pcData;
	}

	public void setPcData(boolean pcData) {
		this.pcData = pcData;
	}

	public boolean isStringValue() {
		return stringValue;
	}

	public void setStringValue(boolean stringValue) {
		this.stringValue = stringValue;
	}

}
