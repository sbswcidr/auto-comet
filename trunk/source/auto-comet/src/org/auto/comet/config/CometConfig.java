package org.auto.comet.config;

/**
 * @author XiaohangHu
 * */
public class CometConfig {

	private String request;

	private Class<?> clazz;

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
