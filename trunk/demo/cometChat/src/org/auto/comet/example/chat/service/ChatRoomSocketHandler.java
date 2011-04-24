package org.auto.comet.example.chat.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.auto.comet.ErrorHandler;
import org.auto.comet.PushException;
import org.auto.comet.Socket;
import org.auto.comet.SocketHandler;
import org.auto.comet.example.chat.service.impl.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author XiaohangHu
 * */
@Controller
public class ChatRoomSocketHandler implements SocketHandler {

	@Autowired
	private ChatRoomService chatRoomService;

	private Map<Serializable, Socket> socketMapping = new HashMap<Serializable, Socket>();

	@Override
	public boolean accept(Socket socket, HttpServletRequest request) {
		String userId = request.getParameter("userId");

		if (socketMapping.get(userId) != null) {
			return false;
		}
		socket.setErrorHandler(new ErrorHandler() {
			@Override
			public void error(Socket socket, PushException e) {
				romveSocket(socket);
				throw e;
			}
		});
		socketMapping.put(userId, socket);
		return true;
		// return false;
	}

	@Override
	public void quit(Socket socket, HttpServletRequest request) {
		String userId = request.getParameter("userId");
		socketMapping.remove(userId);
		this.chatRoomService.loginOut(userId);
	}

	private void romveSocket(Socket socket) {
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			Socket value = entry.getValue();
			if (value.equals(socket)) {
				Serializable key = entry.getKey();
				socketMapping.remove(key);
			}
		}
	}

	public void sendMessage(Serializable id, String msg) {
		Socket socket = this.socketMapping.get(id);
		if (null != socket) {
			socket.sendMessage(msg);
		} else {
			throw new RuntimeException("Can't find socket!");
		}
	}

	public void sendMessageExcept(Serializable exceptId, String msg) {
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			Serializable id = entry.getKey();
			Socket socket = entry.getValue();
			if (id.equals(exceptId)) {
				continue;
			}
			socket.sendMessage(msg);
		}
	}

	public void sendMessageAll(String msg) {
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			Socket socket = entry.getValue();
			socket.sendMessage(msg);
		}
	}

	public Set<Serializable> getAllId() {
		Set<Serializable> result = new HashSet<Serializable>();
		for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
			result.add(entry.getKey());
		}
		return result;

	}

	public boolean hasId(Serializable id) {
		return null != this.socketMapping.get(id);
	}
}
