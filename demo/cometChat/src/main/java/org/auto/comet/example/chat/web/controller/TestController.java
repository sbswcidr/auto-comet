package org.auto.comet.example.chat.web.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@RequestMapping(params = "method=testAjax")
	public String testAjax(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("GBK");
		response.setContentType("test/json");
		// response.setCharacterEncoding("UTF-8");
		System.out.println(response.getCharacterEncoding());
//		request.setCharacterEncoding("UTF-8");
		System.out.println(request.getCharacterEncoding());
		System.out.println("request before setCharacterEncoding:"
				+ request.getParameter("name"));
		System.out.println("request after setCharacterEncoding:"
				+ request.getParameter("name"));
		Writer writer = response.getWriter();
		writer.write("中文哦");
		writer.flush();
		return null;
	}

}
