package org.auto.comet.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaohanghu
 * */
public class SendClient {

	static String URL = "http://localhost:8080/auto-comet-demo/testConcurrent.do?method=sendMessageToAll";

	// static String URL =
	// "http://192.168.12.164:8080/auto-comet-demo/testConcurrent.do?method=sendMessageToAll";

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("message", "test");
		String result = HttpClientUtils.doPostMethod(URL, params, "UTF-8");
		System.out.println(result);
	}

}
