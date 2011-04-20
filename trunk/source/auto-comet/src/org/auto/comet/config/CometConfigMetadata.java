package org.auto.comet.config;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author XiaohangHu
 * */
public class CometConfigMetadata {

	public static final String OBJECT_FACTORY_PROPERTY_NAME = "objectFactory";

	private Properties properties = new Properties();

	private Set<CometMetadata> cometMetadatas = new TreeSet<CometMetadata>();

	public void addProperty(String name, String value) {
		this.properties.put(name, value);
	}

	public String getProperty(String name) {
		return this.properties.getProperty(name);
	}

	public Set<CometMetadata> getCometMetadatas() {
		return cometMetadatas;
	}

	public void addCometMetadata(CometMetadata cometMetadata) {
		if (null != cometMetadata) {
			this.cometMetadatas.add(cometMetadata);
		}
	}

}
