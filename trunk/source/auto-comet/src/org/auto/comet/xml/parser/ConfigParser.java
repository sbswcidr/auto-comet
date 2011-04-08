package org.auto.comet.xml.parser;

import java.util.Iterator;

import org.auto.comet.config.CometConfig;
import org.auto.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author huxh
 * @version 1.0
 */
public class ConfigParser {

	public static final String SOURCE_ELEMENT = "source";

	/**
	 * 读取document加入到配置中
	 *
	 * @param config
	 *            数据同步配置
	 * @param document
	 * */
	public void addConfig(CometConfig config, Document document) {
		Element root = document.getDocumentElement();
		// getElementsByTagName方法会迭代获取所有子节点，这里自己做了处理。
		Iterator<Element> iterator = XmlUtil.childElementIterator(root,
				SOURCE_ELEMENT);
		while (iterator.hasNext()) {
			Element ele = iterator.next();
			// Sourc sourcEntity = this.sourcParser.parse(ele);
			// config.addSourcEntity(sourcEntity);
		}
	}

	/**
	 * 读取document转化为配置
	 *
	 * */
	public CometConfig parse(Document document) {
		CometConfig config = new CometConfig();
		addConfig(config, document);
		return config;
	}

	/**
	 * 读取多个document转化为配置
	 *
	 * */
	public CometConfig parse(Document[] documents) {
		CometConfig config = new CometConfig();
		for (Document document : documents) {
			addConfig(config, document);
		}
		// config.getSourcEntitys().size();
		return config;
	}

}
