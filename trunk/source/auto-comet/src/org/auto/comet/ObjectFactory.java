package org.auto.comet;

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
