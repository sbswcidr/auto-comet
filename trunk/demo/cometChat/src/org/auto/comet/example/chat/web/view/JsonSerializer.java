package org.auto.comet.example.chat.web.view;

import java.text.DateFormat;

import org.auto.comet.example.chat.web.util.NumberDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 */
public class JsonSerializer implements ResourceSerializer {

	private Gson gson;

	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		DateFormat dateFormat = new NumberDateFormat();
		gsonBuilder.registerTypeAdapter(java.util.Date.class, dateFormat);
		gsonBuilder.registerTypeAdapter(java.sql.Date.class, dateFormat);
		gsonBuilder.setDateFormat(DateFormat.LONG);
		gson = gsonBuilder.create();
	}

	public String serialization(Object resource) {
		if (null == resource)
			return null;
		return gson.toJson(resource);
	}

}
