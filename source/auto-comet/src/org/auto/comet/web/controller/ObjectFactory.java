package org.auto.comet.web.controller;

import javax.servlet.ServletContext;

/**
 *
 * ControllerFactory
 *
 * @author XiaohangHu
 * */
public interface ObjectFactory {

	void init(ServletContext servletContext);

	Object getObject(String objectName);

}
