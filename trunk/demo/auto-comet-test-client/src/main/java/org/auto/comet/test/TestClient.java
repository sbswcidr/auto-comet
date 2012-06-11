package org.auto.comet.test;

import java.io.IOException;

import org.auto.comet.client.ning.CometClient;

/**
 * @author xiaohanghu
 * */
public class TestClient {

	static String URL = "http://localhost:8080/auto-comet-demo/testConcurrentHandler.comet";

	// static String URL =
	// "http://192.168.12.164:8080/auto-comet-demo/testConcurrentHandler.comet";

	public static void main(String[] args) throws IOException {
		createComet(100);
	}

	public static void createComet(int count) throws IOException {
		for (int i = 0; i < count; i++) {
			CometClient client = new CometClient(URL);
			client.connection(null);
		}
	}
}
