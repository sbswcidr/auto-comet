package org.auto.comet.example.chat.web.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(
			@SuppressWarnings("rawtypes") Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (null == model) {
			return;
		}
		JSONObject obj = JSONObject.fromObject(model);
		Writer writer = response.getWriter();
		writer.write(obj.toString());
		writer.flush();
	}

}
