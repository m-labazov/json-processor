package com.maestro.json.impl.beaninfo.factory.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlBeanInfoDefinitions {

	@XmlElement(name = "beanInfo")
	private List<XmlBeanInfoDefinition> definitions = new ArrayList<XmlBeanInfoDefinition>();

	public List<XmlBeanInfoDefinition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<XmlBeanInfoDefinition> definitions) {
		this.definitions = definitions;
	}

	public static void main(String[] args) throws JAXBException, IOException {
		class MySchemaOutputResolver extends SchemaOutputResolver {
			public Result createOutput(String namespaceUri,
					String suggestedFileName) throws IOException {
				return new StreamResult(new File(".", suggestedFileName));
			}
		}

		JAXBContext context = JAXBContext.newInstance(
				XmlBeanInfoDefinition.class, XmlFieldDefinition.class,
				XmlBeanInfoDefinitions.class);
		context.generateSchema(new MySchemaOutputResolver());
		Unmarshaller unMarsh = context.createUnmarshaller();
		// unMarsh.
		Marshaller marshaller = context.createMarshaller();
		XmlBeanInfoDefinitions xmlBeanInfoDefinitions = new XmlBeanInfoDefinitions();
		XmlBeanInfoDefinition xmlBeanInfoDefinition = new XmlBeanInfoDefinition();
		xmlBeanInfoDefinition.setBeanClassRef("zzz.yyy");
		XmlFieldDefinition xmlFieldDefinition = new XmlFieldDefinition();
		xmlFieldDefinition.setJavaField("dsfsfd");
		xmlFieldDefinition.setXmlAttr("dsfsf");
		xmlBeanInfoDefinition.getFieldDefinitions().add(xmlFieldDefinition);
		XmlBeanInfoDefinition xmlBeanInfoDefinition2 = new XmlBeanInfoDefinition();
		xmlBeanInfoDefinition2.setBeanClassRef("fff.ggg");
		xmlBeanInfoDefinitions.getDefinitions().add(xmlBeanInfoDefinition);
		xmlBeanInfoDefinitions.getDefinitions().add(xmlBeanInfoDefinition2);
		marshaller.marshal(xmlBeanInfoDefinitions, new File("1.xml"));
	}
}
