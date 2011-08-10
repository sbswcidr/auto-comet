package org.auto.comet.example.chat.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.auto.comet.support.JsonProtocolUtils;
import org.auto.comet.xml.PropertiesXmlDefinitionParser;
import org.auto.io.ClassPathResource;
import org.auto.io.FileResource;
import org.auto.io.Resource;
import org.auto.io.scanner.ClassPathResourceScanner;
import org.auto.io.scanner.DefaultFilePatternScanner;
import org.auto.io.scanner.FileHandler;
import org.auto.io.scanner.ResourceHandler;
import org.auto.util.ClassUtils;
import org.auto.util.PropertiesLoaderUtils;
import org.auto.xml.XmlDefinitionManager;
import org.auto.xml.XmlDefinitionResolver;
import org.auto.xml.XmlDocumentLoader;
import org.springframework.ui.ModelMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestComet {
	private static String xmlDefinitionMappingsLocation = "META-INF/auto.schemas";

	public static int count = 0;

	public static void main(String[] args) throws Exception {
		// testJson();
		// testCalendar();
		// testTimer();
		// testClassLoader();
		// testClassPathResourceScanner();
		// testPatternFileScanner();

		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");
		JSONObject obj = JSONObject.fromObject(modelMap);
		List linkedList = new LinkedList();
		// JSONObject obj = JSONObject.fromObject(linkedList);
		System.out.println(JsonProtocolUtils.getConnectionCommend(null));

	}

	public static void testPatternFileScanner() {
		DefaultFilePatternScanner scanner = new DefaultFilePatternScanner();
		FileHandler handler = new FileHandler() {

			@Override
			public void handle(File t) {
				System.out.println(t);
				Resource resource = new FileResource(t);
				resource.getInputStream();
				count++;

			}
		};
		scanner.scan("F:/download/ckeditor_3.5.2.zip \r\n", handler);
		System.out.println("count" + count);
	}

	public static void testClassPathResourceScanner() {
		ClassPathResourceScanner scanner = new ClassPathResourceScanner();

		scanner.scan("org/apache/commons/logging/LogFactory.class",
				new ResourceHandler() {

					@Override
					public void handle(Resource t) {
						t.getInputStream();
						System.out.println(t.getDescription());
						count++;
					}
				});
		System.out.println("count" + count);
	}

	public static void testClassLoader() throws InterruptedException,
			IOException {
		Enumeration<URL> enumeration = ClassUtils.getDefaultClassLoader()
				.getResources("org/auto/");

		while (enumeration.hasMoreElements()) {
			URL url = enumeration.nextElement();
			System.out.println(url);
		}
	}

	public static void testTimer() throws InterruptedException {
		System.out.println(Thread.currentThread().getId() + "!");
		long t = 2000l;
		new Timer(true).schedule(new TimerTask() { // true，表示执行的线程是一个守护线程
					@Override
					public void run() {
						System.out.println("线程"
								+ Thread.currentThread().getId() + "!");
						// System.exit(1);
					}
				}, 1000l, t / 2);
		Thread.sleep(10l);
	}

	public static void testCalendar() {
		Calendar now = Calendar.getInstance();
		System.out.println(now.getTimeInMillis());
		Date date = new Date();
		System.out.println(date.getTime());
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
