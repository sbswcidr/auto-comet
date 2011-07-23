package org.auto.comet.example.chat.web.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.View;

public class JsonView implements View {
	private String contentType = "text/json";

	public String getContentType() {
		return contentType;
	}

	public void render(@SuppressWarnings("rawtypes") Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType(contentType);
		if (null == model) {
			return;
		}
		JSONObject obj = JSONObject.fromObject(model);
		Writer writer = response.getWriter();
		writer.write(obj.toString());
		writer.flush();
	}
}
