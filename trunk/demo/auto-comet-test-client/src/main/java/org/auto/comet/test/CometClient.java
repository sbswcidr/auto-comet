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
	/** �����������ͬ�� */
	public static String SYNCHRONIZE_KEY = "_S_COMET";
	/** ͬ��ֵ���������� */
	public static String CONNECTION_VALUE = "C";
	/** ͬ��ֵ���Ͽ����� */
	public static String DISCONNECT_VALUE = "D";
	/** ���ز�����������ID */
	public static String CONNECTIONID_KEY = "_C_COMET";

	private String url;
	private String cid;

	public CometClient(String url) {
		this.url = url;
	}

	/**
	 * ��ʼ����
	 * 
	 * @param userParam
	 *            ����ʱ���ݸ��������˵Ĳ���
	 * @param success
	 *            ���ӳɹ�������
	 * @param failure
	 *            ����ʧ�ܴ�����
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
			throw new IllegalStateException("�ܾ�����");
		}
		this.cid = cid;
		this.polling(cid);
	}

	/** ��ѯ */
	public void polling(String cid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(CONNECTIONID_KEY, cid);
		String dataString = HttpClientUtils.doGetMethod(url, params, "UTF-8");
		acceptDatas(dataString);
	}

	/** �Ͽ����� */
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
		// ���ܵ����һ����Ϣ
		Object lastData = datas.get(datas.size() - 1);
		// ����ǶϿ�����
		boolean disconnect = this.isDisconnectObj(lastData);
		int len = datas.size();
		if (disconnect) {
			len--;
		}
		if (!disconnect) {// ������ǶϿ����ӣ�������ѯ
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
