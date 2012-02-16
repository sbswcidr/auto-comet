package org.auto.comet.example.chat.web.controller;

import java.io.Serializable;
import java.util.Set;

import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.example.chat.service.UserIdRepeatException;
import org.auto.comet.example.chat.service.impl.ChatRoomService;
import org.auto.comet.example.chat.web.util.JsonResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XiaohangHu
 * */
@Controller
@RequestMapping("/chatRoom.do")
public class ChatRoomController {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private ChatRoomService chatRoomService;

	@RequestMapping(params = "method=sendMessage")
	public void sendMessage(String userId, String message,
			ServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("User [" + userId + "] send message [" + message + "]");
		}
		this.chatRoomService.sendMessage(userId, message);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");
		JsonResultUtils.outJson(modelMap, response);
	}

	@RequestMapping(params = "method=login")
	public void login(String userId, ServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("User login [" + userId + "] .");
		}
		ModelMap modelMap = new ModelMap();
		try {
			this.chatRoomService.login(userId);
		} catch (UserIdRepeatException e) {
			modelMap.addAttribute("success", false);
			JsonResultUtils.outJson(modelMap, response);
			return;
		}
		modelMap.addAttribute("success", true);
		JsonResultUtils.outJson(modelMap, response);
	}

	@RequestMapping(params = "method=getUserList")
	public void getUserList(ServletResponse response) {
		ModelMap modelMap = new ModelMap();
		Set<Serializable> users = this.chatRoomService.getUserList();
		modelMap.addAttribute("users", users);
		JsonResultUtils.outJson(modelMap, response);
	}

}
