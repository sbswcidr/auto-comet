package org.auto.comet.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.auto.comet.web.controller.SocketController;
import org.auto.web.util.RequestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author XiaohangHu
 * */
public abstract class AbstractDispatcherServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -6630054727538426074L;

	protected static Object getBean(String id, ServletContext sc) {
		ApplicationContext context = getWebApplicationContext(sc);
		return context.getBean(id);
	}

	protected static UrlHandlerMapping getUrlHandlerMapping(ServletContext sc) {
		return (UrlHandlerMapping) getBean("cometUrlHandlerMapping", sc);
	}

	protected static ApplicationContext getWebApplicationContext(
			ServletContext sc) {
		return (WebApplicationContext) sc
				.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	protected static SocketController getCometController(HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		UrlHandlerMapping mapping = getUrlHandlerMapping(servletContext);
		String uri = RequestUtils.getServletPath(request);
		Object obj = mapping.getUrlMap().get(uri);
		if (obj instanceof SocketController) {
			return (SocketController) obj;
		}
		return null;
	}

}
