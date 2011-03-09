package org.auto.comet.web;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.PushRuntimeException;
import org.auto.comet.web.listener.SocketEvent;
import org.auto.comet.web.listener.SocketListener;

/**
 * @author XiaohangHu
 * */
public class SocketManager {

	private Map<Serializable, PushSocket> sockets;

	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

	private SecureRandom random;

	{
		sockets = new HashMap<Serializable, PushSocket>();
		try {
			random = SecureRandom.getInstance(RANDOM_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("创建随机数生成器出错,没有找到[" + RANDOM_ALGORITHM
					+ "]算法", e);
		}
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

	PushSocket getSocket(Serializable id) {
		return this.sockets.get(id);
	}

	private PushSocket removeSocket(Serializable id) {
		return this.sockets.remove(id);
	}

	private void addSocket(PushSocket socket) {
		this.sockets.put(socket.getId(), socket);
	};

	private boolean hasSocket(Serializable id) {
		Socket socket = this.getSocket(id);
		return null != socket;
	}

	private PushSocket createSocket() {
		Serializable id = getConnectionId();
		PushSocket socket = new PushSocket(id);
		return socket;
	}

	/**
	 * 接收消息
	 * */
	void receiveMessage(String connectionId, HttpServletRequest request,
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
	PushSocket creatConnection() {
		PushSocket socket = createSocket();
		socket.addListener(new SocketListener() {
			@Override
			public void onReallyClose(SocketEvent event) {
				PushSocket soc = event.getPushSocket();
				Serializable id = soc.getId();
				removeSocket(id);
			}
		});
		this.addSocket(socket);
		return socket;
	}

	/**
	 * 断开链接
	 * */
	void disconnect(String connectionId) {
		PushSocket socket = this.getSocket(connectionId);
		socket.close();
	}

}
