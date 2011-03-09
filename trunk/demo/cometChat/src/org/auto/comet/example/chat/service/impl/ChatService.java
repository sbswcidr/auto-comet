package org.auto.comet.example.chat.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.auto.comet.example.chat.service.IChatService;
import org.auto.comet.service.CometService;
import org.auto.comet.web.Socket;
import org.auto.comet.web.RequestParameter;
import org.springframework.stereotype.Service;

/**
 * ChatService
 * <p>
 * CreateTime: 2010-6-26
 * </p>
 *
 * @author XiaohangHu
 */
@Service
public class ChatService implements IChatService, CometService {

	public static final String COMMAND_KEY = "command";
	public static final String COMMAND_LOGIN = "login";
	public static final String COMMAND_RECEIVE = "receive";

	private Map<Serializable, Socket> socketMapping = new HashMap<Serializable, Socket>();

	@Override
	public void accept(Socket pushSocket, RequestParameter requestParameter) {
		String userId = requestParameter.getParameter("userId");
		socketMapping.put(userId, pushSocket);
		this.login(userId);
	}

	@Override
	public void quit(Socket socket) {
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			Socket value = entry.getValue();
			if (value.equals(socket)) {
				Serializable key = entry.getKey();
				socketMapping.remove(key);
			}
		}
	}

	@Override
	public void exit(Serializable userId) {
		socketMapping.remove(userId);
	}

	@Override
	public void login(Serializable userId) {
		// 登录了给所有在线的人发广播
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			Socket socket = entry.getValue();
			JSONObject message = new JSONObject();
			message.put(COMMAND_KEY, COMMAND_LOGIN);
			message.put("userId", userId);
			socket.sendMessage(message.toString());
		}
	}

	@Override
	public void sendMessage(Serializable userId, String message) {
		Socket socket = socketMapping.get(userId);
		JSONObject result = new JSONObject();
		result.put(COMMAND_KEY, COMMAND_RECEIVE);
		result.put("userId", userId);
		result.put("text", message);
		socket.sendMessage(result.toString());
	}

	@Override
	public void sendMessage(Serializable[] userIds, String message) {
		for (Serializable userId : userIds) {
			Socket socket = socketMapping.get(userId);
			socket.sendMessage(message);
		}
	}

}
