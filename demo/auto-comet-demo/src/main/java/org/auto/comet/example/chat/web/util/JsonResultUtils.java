package org.auto.comet.example.chat.web.util;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;

import javax.servlet.ServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by IntelliJ IDEA. User: xiaohanghu Date: 11-7-23 Time: ����6:46 To
 * change this template use File | Settings | File Templates.
 */
public class JsonResultUtils {

	public static final String CONTENT_TYPE_JSON = "text/json";
	private static Gson gson;

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(DateFormat.LONG);
		gson = gsonBuilder.create();
	}

	public static void outJson(Object data, ServletResponse response) {
		response.setContentType(CONTENT_TYPE_JSON);
		if (null == data) {
			return;
		}

		String result = gson.toJson(data);
		Writer writer;
		try {
			writer = response.getWriter();
			writer.write(result);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
