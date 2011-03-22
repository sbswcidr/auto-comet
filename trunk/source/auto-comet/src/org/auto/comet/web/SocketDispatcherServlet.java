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
import org.auto.comet.web.controller.SocketController;

/**
 * @author XiaohangHu
 * */
public class SocketDispatcherServlet extends AbstractDispatcherServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -3671690949937300581L;

	protected SocketManager creatRequestHandle(ServletContext servletContext) {
		SocketManager requestHandle = new SocketManager(
				getSocketStore(servletContext));
		return requestHandle;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext servletContext = request.getServletContext();
		SocketManager requestHandle = creatRequestHandle(servletContext);

		String synchronizValue = getSynchronizValue(request);
		if (null == synchronizValue) {// 同步值为空则为接收消息
			receiveMessage(requestHandle, request, response);
		} else if (Protocol.CONNECTION_VALUE.equals(synchronizValue)) {// 创建链接请求
			creatConnection(requestHandle, request, response);
		} else if (Protocol.DISCONNECT_VALUE.equals(synchronizValue)) {// 断开链接请求
			disconnect(requestHandle, request);
		}
	}

	/**
	 * 接收消息
	 * */
	private static void receiveMessage(SocketManager requestHandle,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String connectionId = getConnectionId(request);
		requestHandle.receiveMessage(connectionId, request, response);
	}

	private static void creatConnection(SocketManager requestHandle,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SocketController service = getCometService(request);
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		PushSocket socket = requestHandle.creatConnection();
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
	private static void disconnect(SocketManager requestHandle,
			HttpServletRequest request) {
		SocketController service = getCometService(request);
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		String connectionId = getConnectionId(request);
		requestHandle.disconnect(connectionId, service, request);
	}

	private static String getSynchronizValue(HttpServletRequest request) {
		// StringUtils.trim();
		return request.getParameter(Protocol.SYNCHRONIZE_KEY);
	}

	private static String getConnectionId(HttpServletRequest request) {
		return request.getParameter(Protocol.CONNECTIONID_KEY);
	}

	/**
	 * manager 存放在 servletContext
	 * */
	private static LocalSocketStore getSocketStore(ServletContext servletContext) {
		LocalSocketStore cometManager = (LocalSocketStore) servletContext
				.getAttribute(ServletContextKey.SOCKET_STORE_KEY);
		if (null == cometManager) {// 延迟初始化
			cometManager = new LocalSocketStore();
			servletContext.setAttribute(ServletContextKey.SOCKET_STORE_KEY,
					cometManager);
		}
		return cometManager;
	}
}
