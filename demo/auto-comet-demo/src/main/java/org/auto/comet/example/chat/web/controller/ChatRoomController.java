package org.auto.comet.example.chat.web.controller;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.example.chat.service.UserIdRepeatException;
import org.auto.comet.example.chat.service.impl.ChatRoomService;
import org.auto.comet.example.chat.web.view.ResourceModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView sendMessage(String userId, String message) {
		if (log.isDebugEnabled()) {
			log.debug("User [" + userId + "] send message [" + message + "]");
		}
		this.chatRoomService.sendMessage(userId, message);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");
		return new ResourceModelAndView(modelMap);
	}

	@RequestMapping(params = "method=login")
	public ModelAndView login(String userId) {
		if (log.isDebugEnabled()) {
			log.debug("User login [" + userId + "] .");
		}
		ModelMap modelMap = new ModelMap();
		try {
			this.chatRoomService.login(userId);
		} catch (UserIdRepeatException e) {
			modelMap.addAttribute("success", false);
			return new ModelAndView("jsonView", modelMap);
		}
		modelMap.addAttribute("success", true);
		return new ResourceModelAndView(modelMap);
	}

	@RequestMapping(params = "method=getUserList")
	public ModelAndView getUserList() {
		ModelMap modelMap = new ModelMap();
		Set<Serializable> users = this.chatRoomService.getUserList();
		modelMap.addAttribute("users", users);
		return new ResourceModelAndView(modelMap);
	}

}
