package org.auto.comet.config;

/**
 * @author XiaohangHu
 * */
public class ConfigManager {

	private AutoCometConfig config = null;

	public ConfigManager(AutoCometConfig config) {
		this.config = config;
	}

	public AutoCometConfig getConfig() {
		return this.config;
	}

}
