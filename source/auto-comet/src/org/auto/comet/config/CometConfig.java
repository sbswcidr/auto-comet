package org.auto.comet.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiaohangHu
 * */
public class CometConfig {

	private Map<String, String> constant = new HashMap<String, String>();

	public void addConstant(String name, String value) {
		this.constant.put(name, value);
	}

	public String getConstant(String name) {
		return this.constant.get(name);
	}

}
