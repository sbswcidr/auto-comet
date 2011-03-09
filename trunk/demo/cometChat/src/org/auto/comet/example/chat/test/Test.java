package org.auto.comet.example.chat.test;

import org.auto.json.JsonObject;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testJson();
	}

	public static void testJson() {
		JSONObject obj = new JSONObject();
		obj.put("asdf", "sdfasdf");

		JsonObject jsonObject = new JsonObject();

		jsonObject.put("asdf", "sdfasdf");

		System.out.println(jsonObject.toString());

	}
}
