package org.auto.comet.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * @author XiaohangHu
 * */
public class UrlHandlerMapping extends ApplicationObjectSupport {
	private final Map<Object, Object> urlMap = new HashMap<Object, Object>();
	Properties mappings = null;

	public void setMappings(Properties mappings) {
		this.mappings = mappings;

	}

	public void setUrlMap(Map<Object, Object> urlMap) {
		this.urlMap.putAll(urlMap);
	}

	/**
	 * Allow Map access to the URL path mappings, with the option to add or
	 * override specific entries.
	 * <p>
	 * Useful for specifying entries directly, for example via "urlMap[myKey]".
	 * This is particularly useful for adding or overriding entries in child
	 * bean definitions.
	 */
	public Map<Object, Object> getUrlMap() {
		return this.urlMap;
	}

	public void init() {
		registerHandler(mappings);
		this.mappings = null;
	}

	public void registerHandler(Properties mappings) {
		for (Entry<Object, Object> entry : mappings.entrySet()) {
			String key = (String) entry.getKey();
			String handlerName = (String) entry.getValue();
			ApplicationContext context = getApplicationContext();
			if (context.isSingleton(handlerName)) {
				Object value = context.getBean(handlerName);
				this.urlMap.put(key, value);
			}
		}
	}

}
