package org.auto.comet.web.controller;

import javax.servlet.ServletContext;

/**
 *
 * ControllerFactory
 *
 * @author XiaohangHu
 * */
public interface ControllerFactory {

	void init(ServletContext servletContext);

	SocketController getController(String controllerName);

}
