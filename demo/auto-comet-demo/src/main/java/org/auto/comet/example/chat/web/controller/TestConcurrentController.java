package org.auto.comet.example.chat.web.controller;

import javax.servlet.ServletResponse;

import org.auto.comet.example.chat.service.TestConcurrentHandler;
import org.auto.comet.example.chat.web.util.JsonResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XiaohangHu
 * */
@Controller
@RequestMapping("/testConcurrent.do")
public class TestConcurrentController {

	@Autowired
	private TestConcurrentHandler testConcurrentHandler;

	@RequestMapping(params = "method=sendMessageToAll")
	public void sendMessageToAll(String message, ServletResponse response) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");
		this.testConcurrentHandler.sendToAll(message);
		JsonResultUtils.outJson(modelMap, response);
	}

}
