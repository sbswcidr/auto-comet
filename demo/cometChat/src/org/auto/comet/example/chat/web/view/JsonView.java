package org.auto.comet.example.chat.web.view;

import net.sf.json.JSONObject;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Map;

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
		Object jsonData = model.get(JsonModelAndView.JSON_DATA_ATTRIBUTE_NAME);
		if (null == jsonData)
			return;
		JSONObject obj = JSONObject.fromObject(jsonData);
		Writer writer = response.getWriter();
		writer.write(obj.toString());
		writer.flush();
	}
}
