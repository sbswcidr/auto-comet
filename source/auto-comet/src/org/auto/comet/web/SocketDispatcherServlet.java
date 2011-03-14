package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.LocalSocketStore;
import org.auto.comet.Protocol;
import org.auto.comet.PushSocket;
import org.auto.comet.RequestHandle;
import org.auto.comet.service.CometService;
import org.auto.json.JsonObject;

/**
 * @author XiaohangHu
 * */
public class SocketDispatcherServlet extends AbstractDispatcherServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -3671690949937300581L;

	protected RequestHandle creatRequestHandle(ServletContext servletContext) {
		RequestHandle requestHandle = new RequestHandle(
				getSocketStore(servletContext));
		return requestHandle;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext servletContext = request.getServletContext();
		RequestHandle requestHandle = creatRequestHandle(servletContext);

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
	private static void receiveMessage(RequestHandle requestHandle,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String connectionId = getConnectionId(request);
		requestHandle.receiveMessage(connectionId, request, response);
	}

	private static void creatConnection(RequestHandle requestHandle,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		CometService service = getCometService(request);
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		PushSocket socket = requestHandle.creatConnection();
		PrintWriter write = response.getWriter();
		// json格式数据
		JsonObject commend = new JsonObject();
		commend.put(Protocol.CONNECTIONID_KEY, socket.getId());
		// 返回生成的链接id
		write.write(commend.toString());
		write.flush();
		Map<String, String[]> parameters = request.getParameterMap();
		RequestParameter rp = new DefaultRequestParameter(parameters);
		service.accept(socket, rp);
	}

	/**
	 * 断开链接
	 * */
	private static void disconnect(RequestHandle requestHandle,
			HttpServletRequest request) {
		CometService service = getCometService(request);
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		String connectionId = getConnectionId(request);
		requestHandle.disconnect(connectionId, service);
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
