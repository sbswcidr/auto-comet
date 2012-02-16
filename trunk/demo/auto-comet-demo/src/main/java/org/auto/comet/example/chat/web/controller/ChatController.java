package org.auto.comet.example.chat.web.controller;

import javax.servlet.ServletResponse;

import org.auto.comet.example.chat.service.IChatService;
import org.auto.comet.example.chat.web.util.JsonResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XiaohangHu
 * */
@Controller
@RequestMapping("/chat.do")
public class ChatController {

	@Autowired
	private IChatService chatService;

	@RequestMapping(params = "method=sendMessage")
	public void sendMessage(String userId, String targetUserId, String message,
			ServletResponse response)  {
		this.chatService.sendMessage(userId, targetUserId, message);
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");

		JsonResultUtils.outJson(modelMap, response);
		// return new ResourceModelAndView(modelMap);
	}

}
