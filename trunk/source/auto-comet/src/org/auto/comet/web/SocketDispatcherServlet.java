package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.auto.comet.JsonProtocolUtils;
import org.auto.comet.LocalSocketStore;
import org.auto.comet.Protocol;
import org.auto.comet.PushSocket;
import org.auto.comet.SocketManager;
import org.auto.comet.SocketStore;
import org.auto.comet.web.controller.SocketController;
import org.auto.web.util.RequestUtils;

/**
 * @author XiaohangHu
 * */
public class SocketDispatcherServlet extends AbstractDispatcherServlet {

	public static final String INIT_PARAMETER_CONFIG_LOCATION = "dispatcherConfigLocation";

	private UrlHandlerMapping urlHandlerMapping;
	/**
	 *
	 */
	private static final long serialVersionUID = -3671690949937300581L;

	public final void init() throws ServletException {
		initHandlerMapping();
		initSocketManager();
	}

	protected void initHandlerMapping() {
		ServletConfig config = getServletConfig();
		String dispatcherConfigLocation = config
				.getInitParameter(INIT_PARAMETER_CONFIG_LOCATION);
		if (StringUtils.isBlank(dispatcherConfigLocation)) {
			dispatcherConfigLocation = getDefaultDispatcherConfigLocation();
		}

	}

	protected void initSocketManager() throws ServletException {
		ServletContext servletContext = getServletContext();
		SocketManager socketManager = creatSocketManager();
		socketManager.startTimer();
		servletContext.setAttribute(ServletContextKey.SOCKET_MANAGER_KEY,
				socketManager);
	}

	public String getDefaultDispatcherConfigLocation() {
		return "/WEB-INF/dispatcher-servlet.xml";
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext servletContext = getServletContext();
		SocketManager socketManager = getSocketManager(servletContext);

		String synchronizValue = getSynchronizValue(request);
		if (null == synchronizValue) {// 同步值为空则为接收消息
			receiveMessage(socketManager, request, response);
		} else if (Protocol.CONNECTION_VALUE.equals(synchronizValue)) {// 创建链接请求
			creatConnection(socketManager, request, response);
		} else if (Protocol.DISCONNECT_VALUE.equals(synchronizValue)) {// 断开链接请求
			disconnect(socketManager, request);
		}
	}

	/**
	 * 接收消息
	 * */
	private static void receiveMessage(SocketManager socketManager,
			HttpServletRequest request, HttpServletResponse response) {
		String connectionId = getConnectionId(request);
		socketManager.receiveMessage(connectionId, request, response);
	}

	private static void creatConnection(SocketManager socketManager,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SocketController service = getCometController(request);
		if (null == service) {
			String uri = RequestUtils.getServletPath(request);
			throw new DispatchException("Cant find comet controller by [" + uri
					+ "]. Did you registered it?");
		}
		PushSocket socket = socketManager.creatConnection();
		PrintWriter write = response.getWriter();
		String commend = JsonProtocolUtils.getConnectionCommend(socket.getId());
		// 返回生成的链接id
		write.write(commend);
		write.flush();
		service.accept(socket, request);
	}

	/**
	 * 断开链接
	 * */
	private static void disconnect(SocketManager socketManager,
			HttpServletRequest request) {
		SocketController service = getCometController(request);
		if (null == service) {
			String uri = RequestUtils.getServletPath(request);
			throw new DispatchException("Cant find comet controller by [" + uri
					+ "]. Did you registered it?");
		}
		String connectionId = getConnectionId(request);
		socketManager.disconnect(connectionId, service, request);
	}

	private static String getSynchronizValue(HttpServletRequest request) {
		// StringUtils.trim();
		return request.getParameter(Protocol.SYNCHRONIZE_KEY);
	}

	private static String getConnectionId(HttpServletRequest request) {
		return request.getParameter(Protocol.CONNECTIONID_KEY);
	}

	protected static SocketManager creatSocketManager() {
		SocketStore socketStore = new LocalSocketStore();
		SocketManager socketManager = new SocketManager(socketStore);
		return socketManager;
	}

	private static SocketManager getSocketManagerFromComtext(
			ServletContext servletContext) {
		SocketManager socketManager = (SocketManager) servletContext
				.getAttribute(ServletContextKey.SOCKET_MANAGER_KEY);
		return socketManager;
	}

	/**
	 * manager 存放在 servletContext
	 * */
	private static SocketManager getSocketManager(ServletContext servletContext) {
		SocketManager socketManager = getSocketManagerFromComtext(servletContext);
		if (null == socketManager) {
			throw new DispatchException("Cant find socketManager!");
		}
		return socketManager;
	}
}
