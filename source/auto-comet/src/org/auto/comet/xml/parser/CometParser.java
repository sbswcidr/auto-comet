package org.auto.comet.xml.parser;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.config.CometConfig;
import org.auto.xml.XmlUtil;
import org.w3c.dom.Element;

/**
 *
 * @author huxh
 * @version 1.0
 */
public class CometParser {

	private static final Log logger = LogFactory.getLog(CometParser.class);

	public static final String REQUEST_ATTRIBUTE = "request";

	public static final String CLASS_ATTRIBUTE = "class";

	/**
	 * 将Element转化为Sourc
	 * */
	public CometConfig parse(Element element) {

		CometConfig cometConfig = new CometConfig();

		setProperty(cometConfig, element);

		// Iterator<Element> iterator = XmlUtil.childElementIterator(element,
		// TARGET_ELEMENT);
		// while (iterator.hasNext()) {
		// Element ele = iterator.next();
		// Target targetEntity = this.targetParser.parse(ele);
		// sourc.addTargetEntity(targetEntity);
		// }

		return cometConfig;
	}

	private Class<?> getClassByName(String className) {
		try {
			return ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void setProperty(CometConfig cometConfig, Element element) {
		String className = XmlUtil.getElementAttributeTrim(CLASS_ATTRIBUTE,
				element);
		String request = XmlUtil.getElementAttributeTrim(REQUEST_ATTRIBUTE,
				element);
		if (StringUtils.isNotBlank(request)) {
			cometConfig.setRequest(request);
		}
		if (StringUtils.isNotBlank(className)) {
			cometConfig.setClazz(getClassByName(className));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("读取配置 : " + className);
		}
	}
}
