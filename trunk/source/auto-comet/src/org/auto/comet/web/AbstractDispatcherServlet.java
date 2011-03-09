package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.service.CometService;
import org.auto.json.JsonObject;
import org.auto.web.util.RequestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author XiaohangHu
 * */
public abstract class AbstractDispatcherServlet extends HttpServlet {

	/** 请求参数名：链接ID */
	public static final String CONNECTIONID_KEY = "_C_COMET";
	/** 请求参数名：同步 */
	public static final String SYNCHRONIZE_KEY = "_S_COMET";

	/** 同步值：创建链接 */
	public static final String CONNECTION_VALUE = "C";
	/** 同步值：断开链接 */
	public static final String DISCONNECT_VALUE = "D";

	/**
	 *
	 */
	private static final long serialVersionUID = -4782358056174321521L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("进入CreateComet的时间：" + new Date() + ".");

		SocketManager socketManager = this.getSocketManager(request);

		String synchronizValue = getSynchronizValue(request);
		if (null == synchronizValue) {// 同步值为空则为接收消息
			receiveMessage(request, response, socketManager);
		} else if (CONNECTION_VALUE.equals(synchronizValue)) {// 创建链接请求
			creatConnection(request, response, socketManager);
		} else if (DISCONNECT_VALUE.equals(synchronizValue)) {// 断开链接请求
			disconnect(request, socketManager);
		}
	}

	private Object getBean(String id, ServletContext sc) {
		ApplicationContext context = getWebApplicationContext(sc);
		return context.getBean(id);
	}

	private UrlHandlerMapping getUrlHandlerMapping(ServletContext sc) {
		return (UrlHandlerMapping) getBean("cometUrlHandlerMapping", sc);
	}

	private ApplicationContext getWebApplicationContext(ServletContext sc) {
		return (WebApplicationContext) sc
				.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	/**
	 * 接收消息
	 * */
	private void receiveMessage(HttpServletRequest request,
			HttpServletResponse response, SocketManager socketManager)
			throws IOException {
		String connectionId = getConnectionId(request);
		socketManager.receiveMessage(connectionId, request, response);
	}

	private CometService getCometService(HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		UrlHandlerMapping mapping = this.getUrlHandlerMapping(servletContext);
		String uri = RequestUtils.getServletPath(request);
		Object obj = mapping.getUrlMap().get(uri);
		if (obj instanceof CometService) {
			return (CometService) obj;
		}
		return null;
	}

	private void creatConnection(HttpServletRequest request,
			HttpServletResponse response, SocketManager socketManager)
			throws IOException {
		PushSocket socket = socketManager.creatConnection();
		CometService service = getCometService(request);
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		PrintWriter write = response.getWriter();
		// json格式数据
		JsonObject commend = new JsonObject();
		commend.put(CONNECTIONID_KEY, socket.getId());
		write.write(commend.toString());
		write.flush();
		Map<String, String[]> parameters = request.getParameterMap();
		RequestParameter rp = new DefaultRequestParameter(parameters);
		service.accept(socket, rp);
	}

	/**
	 * 断开链接
	 * */
	private void disconnect(HttpServletRequest request,
			SocketManager socketManager) {
		CometService service = getCometService(request);
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		String connectionId = getConnectionId(request);
		Socket socket = socketManager.getSocket(connectionId);
		service.quit(socket);
		socketManager.disconnect(connectionId);
	}

	private String getSynchronizValue(HttpServletRequest request) {
		// StringUtils.trim();
		return request.getParameter(SYNCHRONIZE_KEY);
	}

	private String getConnectionId(HttpServletRequest request) {
		return request.getParameter(CONNECTIONID_KEY);
	}

	private SocketManager getSocketManager(HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		return getSocketManager(servletContext);
	}

	/**
	 *
	 * manager 存放在 servletContext
	 * */
	private SocketManager getSocketManager(ServletContext servletContext) {
		SocketManager cometManager = (SocketManager) servletContext
				.getAttribute("CometManager");
		if (null == cometManager) {// 延迟初始化
			cometManager = new SocketManager();
			servletContext.setAttribute("CometManager", cometManager);
		}
		return cometManager;
	}
}
