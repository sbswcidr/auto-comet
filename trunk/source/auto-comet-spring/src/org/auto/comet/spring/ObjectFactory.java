package org.auto.comet.spring;

import javax.servlet.ServletContext;

import org.auto.comet.web.controller.SocketController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author XiaohangHu
 * */
public class ObjectFactory implements
		org.auto.comet.web.controller.ObjectFactory {

	private WebApplicationContext context;

	@Override
	public void init(ServletContext servletContext) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		this.context = context;
	}

	@Override
	public Object getObject(String objectName) {

		Object controller = null;
		controller = context.getBean(objectName);
		if (controller instanceof SocketController) {
			return (SocketController) controller;
		} else {
			throw new ClassCastException("controller must implements ["
					+ SocketController.class.getName() + "]!");
		}
	}

}
