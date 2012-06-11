package org.auto.comet.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

	private String url;
	private String cid;

	public CometClient(String url) {
		this.url = url;
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
	 * 
	 */
	public void connection(Map<String, String> userParam) {
		if (null == userParam) {
			userParam = new HashMap<String, String>();
		}
		userParam.put(SYNCHRONIZE_KEY, CONNECTION_VALUE);
		String result = HttpClientUtils.doPostMethod(url, userParam, "UTF-8");
		// System.out.println(result);

		JSONObject jsonReslult = JSONObject.fromObject(result);
		String cid = jsonReslult.getString(CONNECTIONID_KEY);
		if (StringUtils.isBlank(cid)) {
			throw new IllegalStateException("拒绝连接");
		}
		this.cid = cid;
		this.polling(cid);
	}

	/** 轮询 */
	public void polling(String cid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(CONNECTIONID_KEY, cid);
		String dataString = HttpClientUtils.doGetMethod(url, params, "UTF-8");
		acceptDatas(dataString);
	}

	/** 断开连接 */
	public void disconnect(Map<String, String> userParam) {
		if (null == userParam) {
			userParam = new HashMap<String, String>();
		}
		userParam.put(SYNCHRONIZE_KEY, DISCONNECT_VALUE);
		userParam.put(CONNECTIONID_KEY, cid);

		HttpClientUtils.doGetMethod(url, userParam, "UTF-8");
	}

	public void acceptDatas(String dataString) {
		System.out.println(dataString);
		JSONArray datas = JSONArray.fromObject(dataString);
		// 接受的最后一个消息
		Object lastData = datas.get(datas.size() - 1);
		// 如果是断开连接
		boolean disconnect = this.isDisconnectObj(lastData);
		int len = datas.size();
		if (disconnect) {
			len--;
		}
		if (!disconnect) {// 如果不是断开连接，继续轮询
			this.acceptDatasByLength(datas, len);
			this.polling(cid);
		} else {
			this.acceptDatasByLength(datas, len);
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
