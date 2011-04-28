package org.auto.comet.example.chat.web.controller;

import org.auto.comet.example.chat.service.TestConcurrentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author XiaohangHu
 * */
@Controller
@RequestMapping("/testConcurrent.do")
public class TestConcurrentController {

	@Autowired
	private TestConcurrentHandler testConcurrentHandler;

	@RequestMapping(params = "method=sendMessageToAll")
	public ModelAndView sendMessageToAll(String message) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("success", "true");
		this.testConcurrentHandler.sendToAll(message);
		return new ModelAndView("jsonView", modelMap);
	}

}
