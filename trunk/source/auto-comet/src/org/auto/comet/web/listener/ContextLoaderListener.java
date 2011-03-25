package org.auto.comet.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.LocalSocketStore;
import org.auto.comet.SocketManager;
import org.auto.comet.SocketStore;
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
		initSocketManager(servletContext);
		logger.info("comet init context.");
	}

	private static void initSocketManager(ServletContext servletContext) {
		SocketStore socketStore = new LocalSocketStore();
		SocketManager socketManager = new SocketManager(socketStore);
		socketManager.startTimer();
		servletContext.setAttribute(ServletContextKey.SOCKET_MANAGER_KEY,
				socketStore);
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();
		servletContext.removeAttribute(ServletContextKey.SOCKET_MANAGER_KEY);
	}

}
