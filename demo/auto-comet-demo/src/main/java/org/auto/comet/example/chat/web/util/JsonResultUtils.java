package org.auto.comet.example.chat.web.util;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA. User: xiaohanghu Date: 11-7-23 Time: ����6:46 To
 * change this template use File | Settings | File Templates.
 */
public class JsonResultUtils {

	public static final String CONTENT_TYPE_JSON = "text/json";

	public static void outJson(Object data, HttpServletResponse response)
			throws IOException {
		response.setContentType(CONTENT_TYPE_JSON);
		if (null == data) {
			return;
		}
		JSONObject jsonObject = JSONObject.fromObject(data);
		Writer writer = response.getWriter();
		writer.write(jsonObject.toString());
		writer.flush();
	}
}
