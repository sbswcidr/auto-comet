package org.auto.comet.example.chat.web.controller;

import org.auto.comet.example.chat.web.view.ResourceModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author XiaohangHu
 * */
@Controller
@RequestMapping("/example.do")
public class TestController {

	@RequestMapping(params = "method=sayHelloJson")
	public ModelAndView sayHelloJson(@RequestParam("id") Integer id) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("hello", "hello " + id);
		System.out.println("sayHelloJson req");
		return new ResourceModelAndView(modelMap);
	}

	@RequestMapping(params = "method=listBoardTopic")
	public String listBoardTopic(Integer id, ModelMap model) {
		System.out.println("topicId:" + id);
		return "admin/testSpringMVC";
	}

}
