package org.auto.comet.spring;

import javax.servlet.ServletContext;

import org.auto.comet.web.controller.SocketController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author XiaohangHu
 * */
public class ControllerFactory implements
		org.auto.comet.web.controller.ControllerFactory {

	private WebApplicationContext context;

	@Override
	public void init(ServletContext servletContext) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		this.context = context;
	}

	@Override
	public SocketController getController(String controllerName) {

		Object controller = null;
		controller = context.getBean(controllerName);
		if (controller instanceof SocketController) {
			return (SocketController) controller;
		} else {
			throw new ClassCastException("controller must implements ["
					+ SocketController.class.getName() + "]!");
		}
	}

}
