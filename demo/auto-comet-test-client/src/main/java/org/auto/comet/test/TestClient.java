package org.auto.comet.test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.auto.comet.client.ning.CometClient;
import org.auto.comet.client.ning.ConnectionEvent;
import org.auto.comet.client.ning.ConnectionListener;

import com.ning.http.client.AsyncHttpClient;

/**
 * @author xiaohanghu
 * */
public class TestClient {

	static String URL = "http://localhost:8080/auto-comet-demo/testConcurrentHandler.comet";
	private static final ThreadMXBean threadMXBean = ManagementFactory
			.getThreadMXBean();

	// static String URL =
	// "http://192.168.12.164:8080/auto-comet-demo/testConcurrentHandler.comet";

	public static void main(String[] args) throws Exception {
		createComet(10000);
	}

	public static void createComet(int count) throws IOException,
			InterruptedException {
		ConnectionListener connectionListener = new ConnectionListenerImpl();
		Map<String, String> userParam = new HashMap<String, String>();
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		for (int i = 0; i < count; i++) {
			CometClient client = new CometClient(URL, asyncHttpClient);
			userParam.put("userId", "user" + i);
			client.connection(userParam, connectionListener);
			int threadCount = threadMXBean.getThreadCount();
			// System.out.println("Thread count:" + threadCount);
			Thread.sleep(10);
		}
	}

	static class ConnectionListenerImpl implements ConnectionListener {
		static private AtomicInteger sequence = new AtomicInteger(0);

		@Override
		public void onConnectioned(ConnectionEvent connectionEvent) {
			int count = sequence.addAndGet(1);
			System.out.println("Connection count:" + count);
		}

		@Override
		public void onConnectionFailed(ConnectionEvent connectionEvent) {
			System.err.println("连接失败");
		}
	}
}
