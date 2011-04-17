package org.auto.comet.xml;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.auto.io.Resource;
import org.auto.util.PropertiesLoaderUtils;
import org.auto.xml.DocumentLoader;
import org.auto.xml.XmlDefinitionManager;
import org.auto.xml.XmlDefinitionResolver;
import org.auto.xml.XmlDocumentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author XiaohangHu
 * */
public class SimpleDocumentReader {

	private String xmlDefinitionMappingsLocation = "META-INF/auto.comet.scehmas";

	private DocumentLoader documentLoader;

	{
		initDocumentLoader();
	}

	public SimpleDocumentReader(String xmlDefinitionMappingsLocation) {
		this.xmlDefinitionMappingsLocation = xmlDefinitionMappingsLocation;
		initDocumentLoader();
	}

	private void initDocumentLoader() {
		XmlDocumentLoader documentLoader = new XmlDocumentLoader();
		documentLoader.setValidationMode(XmlDocumentLoader.VALIDATION_NONE);
		documentLoader.setNamespaceAware(true);

		XmlDefinitionResolver delegatingResolver = new XmlDefinitionResolver();

		XmlDefinitionManager xmlDefinitionManager = new XmlDefinitionManager();
		Properties properties = PropertiesLoaderUtils.loadClassPathProperties(
				xmlDefinitionMappingsLocation, null);
		PropertiesXmlDefinitionParser propertiesXmlDefinitionParser = new PropertiesXmlDefinitionParser();
		xmlDefinitionManager
				.addDefinitionMappings(propertiesXmlDefinitionParser
						.parser(properties));

		delegatingResolver.setXmlDefinitionManager(xmlDefinitionManager);

		documentLoader.setEntityResolver(delegatingResolver);
		this.documentLoader = documentLoader;
	}

	public Element read(Resource resource) {
		InputStream in;
		in = resource.getInputStream();
		Document document = documentLoader.loadDocument(in);
		Element element = document.getDocumentElement();
		return element;
	}

	public Set<Element> read(Set<Resource> resources) {
		Set<Element> elements = new TreeSet<Element>();
		for (Resource resource : resources) {
			Element e = read(resource);
			if (null != e) {
				elements.add(e);
			}
		}
		return elements;
	}

}
