package org.auto.comet.test;

/**
 * @author xiaohanghu
 * */
public class TestClient {

	static String URL = "http://localhost:8080/auto-comet-demo/testConcurrentHandler.comet";

	// static String URL =
	// "http://192.168.12.164:8080/auto-comet-demo/testConcurrentHandler.comet";

	public static void main(String[] args) {
		createComet(100);
	}

	public static void createComet(int count) {
		for (int i = 0; i < count; i++) {
			CometClient client = new CometClient(URL);
			client.connection(null);
		}
	}
}
