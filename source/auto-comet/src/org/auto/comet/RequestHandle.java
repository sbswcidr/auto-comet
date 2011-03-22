package org.auto.comet;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.listener.SocketEvent;
import org.auto.comet.listener.SocketListener;
import org.auto.comet.web.DispatchRuntimeException;
import org.auto.comet.web.controller.SocketController;

/**
 * 该类用于处理各种通信请求
 *
 * @author XiaohangHu
 * */
public class RequestHandle {

	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

	private SocketStore socketStore;

	private SocketListener socketListener = new SocketListener() {
		@Override
		public void onReallyClose(SocketEvent event) {
			PushSocket soc = event.getPushSocket();
			Serializable id = soc.getId();
			removeSocket(id);
		}
	};

	private SecureRandom random;
	{
		try {
			random = SecureRandom.getInstance(RANDOM_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("创建随机数生成器出错,没有找到[" + RANDOM_ALGORITHM
					+ "]算法", e);
		}
	}

	public RequestHandle(SocketStore socketStore) {
		this.socketStore = socketStore;
	}

	private String getRandom() {
		long l = random.nextLong();
		l = l >= 0L ? l : -l;
		return Long.toString(l, 36);
	}

	/**
	 * 返回一个不重复的安全随机id
	 * */
	private String getConnectionId() {
		String id = getRandom();
		while (hasSocket(id)) {
			id = getRandom();
		}
		return id;
	}

	public PushSocket getSocket(Serializable id) {
		return socketStore.getSocket(id);
	}

	private PushSocket removeSocket(Serializable id) {
		return socketStore.removeSocket(id);
	}

	private void addSocket(PushSocket socket) {
		socketStore.addSocket(socket);
	};

	private boolean hasSocket(Serializable id) {
		return socketStore.hasSocket(id);
	}

	private PushSocket createSocket() {
		Serializable id = getConnectionId();
		PushSocket socket = new PushSocket(id);
		return socket;
	}

	/**
	 * 接收消息
	 * */
	public void receiveMessage(String connectionId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PushSocket socket = this.getSocket(connectionId);
		if (null == socket) {
			throw new PushRuntimeException("没有找到socket！");
		}
		socket.receiveRequest(request, response);
	}

	/**
	 * 创建新链接
	 * */
	public PushSocket creatConnection() {
		PushSocket socket = createSocket();
		socket.addListener(socketListener);
		this.addSocket(socket);
		return socket;
	}

	/**
	 * 断开链接
	 * */
	public void disconnect(String connectionId, SocketController service) {
		if (null == service) {
			throw new DispatchRuntimeException("没有找到service");
		}
		PushSocket socket = getSocket(connectionId);
		service.quit(socket);
		socket.close();
	}
}
