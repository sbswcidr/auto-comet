package org.auto.comet.config;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @author XiaohangHu
 * */
public class AutoCometConfig {

	private Properties properties = new Properties();

	private Collection<CometConfig> cometConfigs = new LinkedList<CometConfig>();

	public void addProperty(String name, String value) {
		this.properties.put(name, value);
	}

	public String getProperty(String name) {
		return this.properties.getProperty(name);
	}

	public Collection<CometConfig> getCometConfigs() {
		return cometConfigs;
	}

	public void addCometConfig(CometConfig cometConfig) {
		this.cometConfigs.add(cometConfig);
	}

}
