package org.auto.comet.example.chat.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.auto.comet.example.chat.service.ChatRoomSocketHandler;
import org.auto.comet.example.chat.service.IChatRoomService;
import org.auto.comet.example.chat.service.UserIdRepeatException;
import org.springframework.beans.factory.annotation.Autowired;
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
		JsonObject message = new JsonObject();
		message.addProperty(COMMAND_KEY, COMMAND_LOGIN);
		message.addProperty("userId", userId.toString());
		this.socketHandler.sendExcept(userId, message.toString());
	}

	public void sendMessage(Serializable userId, String message) {
		JsonObject result = new JsonObject();
		result.addProperty(COMMAND_KEY, COMMAND_RECEIVE);
		result.addProperty("userId", userId.toString());
		result.addProperty("text", message);
		String now = DateFormatUtils.format(new Date(), "HH:mm:ss");
		result.addProperty("time", now);
		socketHandler.sendToAll(result.toString());
	}

	public Set<Serializable> getUserList() {
		return this.socketHandler.getAllId();
	}

	public void loginOut(Serializable userId) {
		// 登录了给所有在线的人发广播
		JsonObject message = new JsonObject();
		message.addProperty(COMMAND_KEY, COMMAND_LOGINOUT);
		message.addProperty("userId", userId.toString());
		this.socketHandler.sendExcept(userId, message.toString());
	}

}
