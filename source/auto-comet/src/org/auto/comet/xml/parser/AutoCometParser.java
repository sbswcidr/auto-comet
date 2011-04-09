package org.auto.comet.xml.parser;

import java.util.Iterator;

import org.auto.comet.config.AutoCometConfig;
import org.auto.comet.config.CometConfig;
import org.auto.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author huxh
 * @version 1.0
 */
public class AutoCometParser {

	public static final String COMET_ELEMENT = "comet";
	public static final String PROPERTY_ELEMENT = "property";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String VALUE_ATTRIBUTE = "value";

	private CometParser cometParser = new CometParser();

	/**
	 * 读取document加入到配置中
	 *
	 * @param config
	 *            数据同步配置
	 * @param document
	 * */
	public void addConfig(AutoCometConfig config, Document document) {
		Element root = document.getDocumentElement();
		Iterator<Element> iterator = XmlUtil.childElementIterator(root);
		while (iterator.hasNext()) {
			Element element = iterator.next();
			String name = element.getNodeName();
			if (PROPERTY_ELEMENT.equals(name)) {
				parseProperty(config, element);
			} else if (COMET_ELEMENT.equals(name)) {
				CometConfig cometConfig = this.cometParser.parse(element);
				config.addCometConfig(cometConfig);
			}
		}
	}

	protected void parseProperty(AutoCometConfig config, Element element) {
		String name = XmlUtil.getElementAttributeTrim(NAME_ATTRIBUTE, element);
		String value = XmlUtil
				.getElementAttributeTrim(VALUE_ATTRIBUTE, element);
		config.addProperty(name, value);
	}

	/**
	 * 读取document转化为配置
	 *
	 * */
	public AutoCometConfig parse(Document document) {
		AutoCometConfig config = new AutoCometConfig();
		addConfig(config, document);
		return config;
	}

	/**
	 * 读取多个document转化为配置
	 *
	 * */
	public AutoCometConfig parse(Document[] documents) {
		AutoCometConfig config = new AutoCometConfig();
		for (Document document : documents) {
			addConfig(config, document);
		}
		return config;
	}

}
