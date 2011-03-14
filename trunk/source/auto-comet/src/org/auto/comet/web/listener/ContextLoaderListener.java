package org.auto.comet.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.LocalSocketStore;
import org.auto.comet.web.ServletContextKey;

/**
 * comet容器加载
 *
 * @author XiaohangHu
 * */
public class ContextLoaderListener implements ServletContextListener {

	protected final Log logger = LogFactory.getLog(this.getClass());

	public void contextInitialized(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();
		LocalSocketStore cometContext = new LocalSocketStore();
		servletContext.setAttribute(ServletContextKey.SOCKET_STORE_KEY,
				cometContext);
		logger.warn("comet init context");
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
	}

}
