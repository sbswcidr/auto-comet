package org.auto.comet.config;

/**
 * @author XiaohangHu
 * */
public class ConfigManager {

	private CometConfigMetadata config = null;

	public ConfigManager(CometConfigMetadata config) {
		this.config = config;
	}

	public CometConfigMetadata getConfig() {
		return this.config;
	}

}
