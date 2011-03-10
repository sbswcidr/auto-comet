package org.auto.comet.example.chat.web.controller;

import org.auto.comet.example.chat.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author XiaohangHu
 * */
@Controller
@RequestMapping("/chat.do")
public class ChatController {

	@Autowired
	private IChatService chatService;

	@RequestMapping(params = "method=sendMessage")
	public ModelAndView sendMessage(String userId, String targetUserId,
			String message) {
		this.chatService.sendMessage(userId, targetUserId, message);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");
		return new ModelAndView("jsonView", modelMap);
	}

}
