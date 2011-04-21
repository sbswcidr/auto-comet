package org.auto.comet.spring;

import javax.servlet.ServletContext;

import org.auto.comet.SocketHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author XiaohangHu
 * */
public class ObjectFactory implements org.auto.comet.ObjectFactory {

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
		if (controller instanceof SocketHandler) {
			return (SocketHandler) controller;
		} else {
			throw new ClassCastException("controller must implements ["
					+ SocketHandler.class.getName() + "]!");
		}
	}

}
