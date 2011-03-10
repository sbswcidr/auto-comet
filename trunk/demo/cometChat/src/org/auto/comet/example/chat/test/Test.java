package org.auto.comet.example.chat.test;

import java.util.Date;

import org.auto.json.JsonObject;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testJson();
	}

	public static void testJson() {
		JSONObject obj = new JSONObject();
		obj.put("asdf", "{\"a\":\"n\"}");

		JsonObject jsonObject = new JsonObject();

		jsonObject.put("asdf", "sdfasdf");

		System.out.println(obj.toString());
		System.out.println(obj.get("asdf"));
		System.out.println(JSONUtils.valueToString("{\"a\":\"n\"}"));
		System.out.println(new Date().toString());

	}
}
