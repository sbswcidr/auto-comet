package org.auto.comet.example.chat.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.auto.comet.ErrorHandler;
import org.auto.comet.PushException;
import org.auto.comet.Socket;
import org.auto.comet.SocketHandler;
import org.auto.comet.example.chat.service.IChatService;
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
public class ChatService implements IChatService, SocketHandler {

	public static final String COMMAND_KEY = "command";
	public static final String COMMAND_LOGIN = "login";
	public static final String COMMAND_RECEIVE = "receive";

	private Map<Serializable, Socket> socketMapping = new HashMap<Serializable, Socket>();

	@Override
	public boolean accept(Socket pushSocket, HttpServletRequest request) {
		String userId = request.getParameter("userId");
		pushSocket.setErrorHandler(new ErrorHandler() {

			@Override
			public void error(Socket socket, PushException e) {
				// TODO Auto-generated method stub

			}
		});
		socketMapping.put(userId, pushSocket);
		this.login(userId);
		return true;
	}

	@Override
	public void quit(Socket socket, HttpServletRequest request) {
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
		Socket userSocket = socketMapping.get(userId);
		// 登录了给所有在线的人发广播
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			Serializable id = entry.getKey();
			Socket socket = entry.getValue();
			if (id.equals(userId)) {
				continue;
			}
			JSONObject message = new JSONObject();
			message.put(COMMAND_KEY, COMMAND_LOGIN);
			message.put("userId", userId);
			socket.send(message.toString());

			JSONObject message2 = new JSONObject();
			message2.put(COMMAND_KEY, COMMAND_LOGIN);
			message2.put("userId", id);
			userSocket.send(message2.toString());
		}

	}

	@Override
	public void sendMessage(String userId, String targetUserId, String message) {
		Socket socket = socketMapping.get(targetUserId);
		JSONObject result = new JSONObject();
		result.put(COMMAND_KEY, COMMAND_RECEIVE);
		result.put("userId", userId);
		result.put("text", message);
		result.put("time", new Date().toString());
		socket.send(result.toString());
	}

	@Override
	public void sendMessage(Serializable[] userIds, String message) {
		for (Serializable userId : userIds) {
			Socket socket = socketMapping.get(userId);
			socket.send(message);
		}
	}

}
