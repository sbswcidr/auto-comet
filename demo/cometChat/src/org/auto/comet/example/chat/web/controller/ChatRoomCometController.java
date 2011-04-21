package org.auto.comet.example.chat.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.auto.comet.ErrorHandler;
import org.auto.comet.PushException;
import org.auto.comet.Socket;
import org.auto.comet.SocketHandler;
import org.auto.comet.example.chat.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author XiaohangHu
 * */
@Controller
public class ChatRoomCometController implements SocketHandler {

	@Autowired
	private IChatService chatService;

	private Map<Serializable, Socket> socketMapping = new HashMap<Serializable, Socket>();

	@Override
	public void accept(Socket socket, HttpServletRequest request) {
		String userId = request.getParameter("userId");
		socket.setErrorHandler(new ErrorHandler() {

			@Override
			public void error(Socket socket, PushException e) {
				// TODO Auto-generated method stub

			}
		});
		socketMapping.put(userId, socket);
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

}
