package org.auto.comet.example.chat.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateFormatUtils;
import org.auto.comet.ErrorHandler;
import org.auto.comet.PushException;
import org.auto.comet.Socket;
import org.auto.comet.SocketHandler;
import org.auto.comet.example.chat.service.IChatService;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

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
			JsonObject message = new JsonObject();
			message.addProperty(COMMAND_KEY, COMMAND_LOGIN);
			message.addProperty("userId", userId.toString());
			socket.send(message.toString());

			JsonObject message2 = new JsonObject();
			message2.addProperty(COMMAND_KEY, COMMAND_LOGIN);
			message2.addProperty("userId", id.toString());
			userSocket.send(message2.toString());
		}

	}

	@Override
	public void sendMessage(String userId, String targetUserId, String message) {
		Socket socket = socketMapping.get(targetUserId);
		JsonObject result = new JsonObject();
		result.addProperty(COMMAND_KEY, COMMAND_RECEIVE);
		result.addProperty("userId", userId);
		result.addProperty("text", message);
		String now = DateFormatUtils.format(new Date(), "HH:mm:ss");
		result.addProperty("time", now);
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
