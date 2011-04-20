package org.auto.comet.web;

import java.util.HashMap;
import java.util.Map;

import org.auto.comet.web.controller.SocketController;

/**
 * @author XiaohangHu
 * */
public class UrlHandlerMapping {

	private final Map<String, SocketController> urlMap = new HashMap<String, SocketController>();

	public SocketController getHandler(String uri) {
		return urlMap.get(uri);
	}

	public void putHandler(String uri, SocketController controller) {
		this.urlMap.put(uri, controller);
	}

}
