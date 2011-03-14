package org.auto.comet.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.auto.comet.Socket;
import org.auto.comet.web.RequestParameter;

/**
 * @author XiaohangHu
 * */
public class DefaultCometService implements CometService {

	private Map<String, Socket> socketMapping = new HashMap<String, Socket>();

	/**
	 * 根据用户名发送消息
	 * */
	public void sendMessage(Serializable userId, String message) {
		Socket socket = socketMapping.get(userId);
		socket.sendMessage(message);
	}

	@Override
	public void accept(Socket socket, RequestParameter requestParameter) {
		String userId = requestParameter.getParameter("userId");
		socketMapping.put(userId, socket);
	}

	@Override
	public void quit(Socket socket) {
		for (Entry<String, Socket> entry : socketMapping.entrySet()) {
			Socket value = entry.getValue();
			if (value.equals(socket)) {
				String key = entry.getKey();
				socketMapping.remove(key);
			}
		}
	}
}
