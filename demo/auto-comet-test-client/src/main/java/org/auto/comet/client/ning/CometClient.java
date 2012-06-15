package org.auto.comet.client.ning;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.test.HttpClientUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

/**
 * @author xiaohanghu
 * */
public class CometClient {
	/** 请求参数名：同步 */
	public static String SYNCHRONIZE_KEY = "_S_COMET";
	/** 同步值：创建连接 */
	public static String CONNECTION_VALUE = "C";
	/** 同步值：断开连接 */
	public static String DISCONNECT_VALUE = "D";
	/** 返回参数名：连接ID */
	public static String CONNECTIONID_KEY = "_C_COMET";

	private static Log log = LogFactory.getLog(CometClient.class);

	private String url;
	private String cid;
	private AsyncHttpClient asyncHttpClient;

	public CometClient(String url, AsyncHttpClient asyncHttpClient) {
		this.url = url;
		this.asyncHttpClient = asyncHttpClient;
	}

	/**
	 * 开始链接
	 * 
	 * @param userParam
	 *            连接时传递给服务器端的参数
	 * @param success
	 *            连接成功处理方法
	 * @param failure
	 *            连接失败处理方法
	 * @throws IOException
	 * 
	 */
	public void connection(Map<String, String> userParam,
			final ConnectionListener connectionListener) throws IOException {
		if (null == userParam) {
			userParam = new HashMap<String, String>();
		}
		userParam.put(SYNCHRONIZE_KEY, CONNECTION_VALUE);

		BoundRequestBuilder boundRequestBuilder = asyncHttpClient
				.preparePost(url);
		for (Entry<String, String> entry : userParam.entrySet()) {
			boundRequestBuilder.addParameter(entry.getKey(), entry.getValue());
		}
		final ConnectionEvent connectionEvent = new ConnectionEvent();
		connectionEvent.setParams(userParam);

		boundRequestBuilder.execute(new AsyncCompletionHandler<Response>() {

			@Override
			public Response onCompleted(Response response) throws Exception {
				String responseBody = response.getResponseBody("UTF-8");
				System.out.println(responseBody);
				// System.out.println(response.getHeaders());
				// System.out.println(Thread.currentThread());
				JSONObject jsonReslult = JSON.parseObject(responseBody);
				String cid = jsonReslult.getString(CONNECTIONID_KEY);
				if (StringUtils.isBlank(cid) || "null".equals(cid)) {
					connectionListener.onConnectionFailed(connectionEvent);
					// throw new IllegalStateException("拒绝连接");
				} else {
					connectionListener.onConnectioned(connectionEvent);
					setCid(cid);
					polling(cid);
				}
				return response;
			}

			@Override
			public void onThrowable(Throwable t) {
				log.error("AsyncCompletionHandler throws exception!", t);
				System.exit(1);
			}
		});

	}

	protected void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * 轮询
	 * 
	 * @throws IOException
	 */
	public void polling(String cid) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put(CONNECTIONID_KEY, cid);

		String paramsStr = HttpClientUtils.getParams(params);
		String url = this.url + "?" + paramsStr;

		BoundRequestBuilder boundRequestBuilder = asyncHttpClient
				.prepareGet(url);
		// for (Entry<String, String> entry : params.entrySet()) {
		// boundRequestBuilder.addParameter(entry.getKey(), entry.getValue());
		// }

		boundRequestBuilder.execute(new AsyncCompletionHandler<Response>() {

			@Override
			public Response onCompleted(Response response) throws Exception {
				String responseBody = response.getResponseBody("UTF-8");
				acceptDatas(responseBody);
				return response;
			}

			@Override
			public void onThrowable(Throwable t) {
				log.error("AsyncCompletionHandler throws exception!", t);
				System.exit(1);
			}
		});

	}

	/**
	 * 断开连接
	 * 
	 * @throws IOException
	 */
	public void disconnect(Map<String, String> userParam) throws IOException {
		if (null == userParam) {
			userParam = new HashMap<String, String>();
		}
		userParam.put(SYNCHRONIZE_KEY, DISCONNECT_VALUE);
		userParam.put(CONNECTIONID_KEY, cid);

		String paramsStr = HttpClientUtils.getParams(userParam);
		String url = this.url + "?" + paramsStr;

		BoundRequestBuilder boundRequestBuilder = asyncHttpClient
				.prepareGet(url);

		boundRequestBuilder.execute(new AsyncCompletionHandler<Response>() {

			@Override
			public Response onCompleted(Response response) throws Exception {
				// String responseBody = response.getResponseBody("UTF-8");
				// ignore
				return response;
			}

			@Override
			public void onThrowable(Throwable t) {
				log.error("AsyncCompletionHandler throws exception!", t);
				System.exit(1);
			}
		});

	}

	public void acceptDatas(String dataString) throws IOException {
		System.out.println("dataString:" + dataString);
		JSONArray datas = JSON.parseArray(dataString);
		// 接受的最后一个消息
		int length = datas.size();
		Object lastData = datas.get(length - 1);
		// 如果是断开连接
		boolean disconnect = this.isDisconnectObj(lastData);
		if (disconnect) {
			length--;
		}
		if (!disconnect) {// 如果不是断开连接，继续轮询
			this.acceptDatasByLength(datas, length);
			this.polling(cid);
		} else {
			this.acceptDatasByLength(datas, length);
		}
	}

	public boolean isDisconnectObj(Object data) {
		if (data instanceof JSONObject) {
			String value = ((JSONObject) data).getString(SYNCHRONIZE_KEY);
			if (DISCONNECT_VALUE.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public void acceptDatasByLength(JSONArray datas, int len) {
		for (int i = 0; i < len; i++) {
			Object data = datas.get(i);

			// TODO ...
			System.out.println("accept data [" + data.toString() + "],id = "
					+ cid);
			// this.accept(data);
		}
	}
}
