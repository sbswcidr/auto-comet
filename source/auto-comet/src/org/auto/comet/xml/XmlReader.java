package org.auto.comet.xml;

import java.io.IOException;
import java.util.Properties;

import org.auto.io.ClassPathResource;
import org.auto.util.PropertiesLoaderUtils;
import org.auto.xml.XmlDefinitionManager;
import org.auto.xml.XmlDefinitionResolver;
import org.auto.xml.XmlDocumentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author XiaohangHu
 * */
public class XmlReader {

	private static String xmlDefinitionMappingsLocation = "META-INF/auto.comet.scehmas";

	void read() throws IOException {
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

		ClassPathResource resource = new ClassPathResource(
				"org//auto//dataSync-ywgy.sync.xml");
		Document document = documentLoader.loadDocument(resource
				.getInputStream());
		Element element = document.getDocumentElement();

	}
}
