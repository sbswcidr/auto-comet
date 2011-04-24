package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.auto.comet.ObjectFactory;
import org.auto.comet.Protocol;
import org.auto.comet.PushSocket;
import org.auto.comet.SocketHandler;
import org.auto.comet.SocketManager;
import org.auto.comet.config.CometConfigMetadata;
import org.auto.comet.support.JsonProtocolUtils;
import org.auto.comet.support.LocalSocketStore;
import org.auto.comet.support.ObjectFactoryBuilder;
import org.auto.comet.support.SocketStore;
import org.auto.comet.xml.XmlConfigResourceHandler;
import org.auto.web.resource.WebResourceScanMachine;
import org.auto.web.util.RequestUtils;

/**
 * <p>
 * 连接转发servlet
 * </p>
 * 该类用于处理所有的连接请求
 *
 *
 * @author XiaohangHu
 * */
public class SocketDispatcherServlet extends AbstractDispatcherServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = -3671690949937300581L;

	public static final String INIT_PARAMETER_CONFIG_LOCATION = "dispatcherConfigLocation";

	private UrlHandlerMapping urlHandlerMapping;
	private SocketManager socketManager;

	public final void init() throws ServletException {
		getServletContext().log(
				"Initializing Auto comet SocketDispatcherServlet '"
						+ getServletName() + "'");
		super.init();
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
		WebResourceScanMachine webResourceScanMachine = new WebResourceScanMachine(
				this.getServletContext());
		CometConfigMetadata cometConfig = new CometConfigMetadata();

		// 扫描将配置元数据放入cometConfig中
		webResourceScanMachine.scanLocations(dispatcherConfigLocation,
				new XmlConfigResourceHandler(cometConfig));

		ObjectFactory objectFactory = ObjectFactoryBuilder.creatObjectFactory(
				cometConfig, getServletContext());

		UrlHandlerMappingBuilder mappingBuilder = new UrlHandlerMappingBuilder(
				objectFactory);

		urlHandlerMapping = mappingBuilder.buildHandlerMapping(cometConfig);

	}

	protected void initSocketManager() throws ServletException {
		SocketManager socketManager = creatSocketManager();
		socketManager.startTimer();
		this.socketManager = socketManager;
	}

	public String getDefaultDispatcherConfigLocation() {
		return "/WEB-INF/dispatcher-servlet.xml";
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SocketManager socketManager = getSocketManager();

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

	private void creatConnection(SocketManager socketManager,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SocketHandler service = getSocketHandler(request);
		if (null == service) {
			String uri = RequestUtils.getServletPath(request);
			throw new DispatchException("Cant find comet handler by [" + uri
					+ "]. Did you registered it?");
		}

		PushSocket socket = new PushSocket();
		boolean accept = service.accept(socket, request);
		PrintWriter write = response.getWriter();
		String commend = null;
		if (accept) {// 如果接受连接请求则创建连接
			socketManager.creatConnection(socket);
			commend = JsonProtocolUtils.getConnectionCommend(socket.getId());
		} else {// 如果拒绝连接请求
			commend = JsonProtocolUtils.getConnectionCommend(null);
		}
		// 返回生成的链接id
		write.write(commend);
		write.flush();
	}

	/**
	 * 断开链接
	 * */
	private void disconnect(SocketManager socketManager,
			HttpServletRequest request) {
		SocketHandler service = getSocketHandler(request);
		if (null == service) {
			String uri = RequestUtils.getServletPath(request);
			throw new DispatchException("Cant find comet handler by [" + uri
					+ "]. Did you registered it?");
		}
		String connectionId = getConnectionId(request);
		socketManager.disconnect(connectionId, service, request);
	}

	protected SocketHandler getSocketHandler(HttpServletRequest request) {
		String uri = RequestUtils.getServletPath(request);
		return urlHandlerMapping.getHandler(uri);
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

	private SocketManager getSocketManager() {
		if (null == socketManager) {
			throw new DispatchException("Cant find socketManager!");
		}
		return socketManager;
	}

}
