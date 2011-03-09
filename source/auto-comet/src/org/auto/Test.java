package org.auto;

import java.io.IOException;
import java.util.Properties;

import org.auto.comet.xml.PropertiesXmlDefinitionParser;
import org.auto.io.ClassPathResource;
import org.auto.util.PropertiesLoaderUtils;
import org.auto.xml.XmlDocumentLoader;
import org.auto.xml.XmlDefinitionManager;
import org.auto.xml.XmlDefinitionResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Test {
	private static String xmlDefinitionMappingsLocation = "META-INF/auto.schemas";

	public static void main(String[] args) throws Exception {
		testJson();
	}

	public static void testJson() {
	}

	public static void testXml() throws IOException {

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

		System.out.println(element.getNamespaceURI());

		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element) {
				Element ele = (Element) node;
				String namespaceUri = ele.getNamespaceURI();
				System.out.println(namespaceUri);
			}
		}
	}

}
