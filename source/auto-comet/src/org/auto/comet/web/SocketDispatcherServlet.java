package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.JsonProtocolUtils;
import org.auto.comet.LocalSocketStore;
import org.auto.comet.Protocol;
import org.auto.comet.PushSocket;
import org.auto.comet.SocketManager;
import org.auto.comet.SocketStore;
import org.auto.comet.web.controller.SocketController;

/**
 * @author XiaohangHu
 * */
public class SocketDispatcherServlet extends AbstractDispatcherServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -3671690949937300581L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext servletContext = request.getServletContext();
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
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String connectionId = getConnectionId(request);
		socketManager.receiveMessage(connectionId, request, response);
	}

	private static void creatConnection(SocketManager socketManager,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SocketController service = getCometService(request);
		if (null == service) {
			throw new DispatchException("没有找到service");
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
		SocketController service = getCometService(request);
		if (null == service) {
			throw new DispatchException("没有找到service");
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
		if (null == socketManager) {// 延迟初始化，双重校验锁。或许有问题
			synchronized (SocketDispatcherServlet.class) {
				socketManager = getSocketManagerFromComtext(servletContext);
				if (null == socketManager) {
					socketManager = creatSocketManager();
					socketManager.startTimer();
					servletContext
							.setAttribute(ServletContextKey.SOCKET_MANAGER_KEY,
									socketManager);
				}
			}
		}
		return socketManager;
	}
}
