package org.auto.comet.example.chat.service.impl;

import org.auto.comet.example.chat.service.IChatRoomService;
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
	public static final String COMMAND_RECEIVE = "receive";

}
