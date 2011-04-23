package org.auto.comet.example.chat.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateFormatUtils;
import org.auto.comet.example.chat.service.ChatRoomSocketHandler;
import org.auto.comet.example.chat.service.IChatRoomService;
import org.auto.comet.example.chat.service.UserIdRepeatException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ChatRoomService implements IChatRoomService {

	public static final String COMMAND_KEY = "command";
	public static final String COMMAND_LOGIN = "login";
	public static final String COMMAND_LOGINOUT = "loginout";
	public static final String COMMAND_RECEIVE = "receive";

	@Autowired
	private ChatRoomSocketHandler socketHandler;

	public void login(Serializable userId) throws UserIdRepeatException {
		if (this.socketHandler.hasId(userId)) {
			throw new UserIdRepeatException();
		}
		// 登录了给所有在线的人发广播
		JSONObject message = new JSONObject();
		message.put(COMMAND_KEY, COMMAND_LOGIN);
		message.put("userId", userId);
		this.socketHandler.sendMessageExcept(userId, message.toString());
	}

	public void sendMessage(Serializable userId, String message) {
		JSONObject result = new JSONObject();
		result.put(COMMAND_KEY, COMMAND_RECEIVE);
		result.put("userId", userId);
		result.put("text", message);
		String now = DateFormatUtils.format(new Date(), "HH:mm:ss");
		result.put("time", now);
		socketHandler.sendMessageAll(result.toString());
	}

	public Set<Serializable> getUserList() {
		return this.socketHandler.getAllId();
	}

	public void loginOut(Serializable userId) {
		// 登录了给所有在线的人发广播
		JSONObject message = new JSONObject();
		message.put(COMMAND_KEY, COMMAND_LOGINOUT);
		message.put("userId", userId);
		this.socketHandler.sendMessageExcept(userId, message.toString());
	}

}
