package org.auto.comet.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

/**
 * @author georgecao
 */
public class HttpClientUtils {
	static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	static int TIMEOUT = 100 * 1000;

	private static HttpClient createHttpClient() {
		HttpClient httpClient = new HttpClient(connectionManager);
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT);
		return httpClient;
	}

	private static void setHead(HttpMethodBase method, String encoding) {
		// 设置header
		method.setRequestHeader("Content-Encoding", "text/html");
		method.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=" + encoding);
		method.setRequestHeader("Connection", "close");
	}

	private static GetMethod createGetMethod(String url, String encoding) {
		GetMethod method = new GetMethod(url);
		setHead(method, encoding);
		return method;
	}

	private static PostMethod createPostMethod(String url, String encoding) {
		PostMethod method = new PostMethod(url);
		setHead(method, encoding);
		return method;
	}

	private static DeleteMethod createDeleteMethod(String url, String encoding) {
		DeleteMethod method = new DeleteMethod(url);
		setHead(method, encoding);
		return method;
	}

	public static String doMethod(HttpClient httpClient, HttpMethodBase method) {
		String ret = "";
		try {
			httpClient.executeMethod(method);
			ret = method.getResponseBodyAsString();
		} catch (Exception e) {
			System.err.println("TwitterHttpClient:postNew failed.");
		} finally {
			method.releaseConnection();
		}
		return ret;
	}

	public static String doGetMethod(String url, Map<String, String> params,
			String encoding) {
		// 设置参数
		HttpClient httpClient = createHttpClient();
		String paramsString = getParams(params);
		if (StringUtils.isNotBlank(paramsString)) {
			url = url + "?" + paramsString;
		}
		HttpMethodBase method = createGetMethod(url, encoding);

		return doMethod(httpClient, method);
	}

	public static String doPostMethod(String url, Map<String, String> params,
			String encoding) {

		HttpClient httpClient = createHttpClient();
		PostMethod postMethod = createPostMethod(url, encoding);

		if (null == params) {
			return doMethod(httpClient, postMethod);
		}

		// 设置参数
		List<NameValuePair> nvList = new ArrayList<NameValuePair>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			nvList.add(new NameValuePair(key, value));
		}

		postMethod.setRequestBody(nvList.toArray(new NameValuePair[] {}));
		return doMethod(httpClient, postMethod);
	}

	public static String doDeleteMethod(String url, Map<String, String> params,
			String encoding) {

		HttpClient httpClient = createHttpClient();
		String paramsString = getParams(params);
		if (StringUtils.isNotBlank(paramsString)) {
			url = url + "?" + params;
		}
		DeleteMethod postMethod = createDeleteMethod(url, encoding);
		return doMethod(httpClient, postMethod);
	}

	public static String getParams(Map<String, String> params) {
		if (null == params)
			return null;
		StringBuilder result = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = params.entrySet()
				.iterator();

		if (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
					.next();
			String key = entry.getKey();
			String value = entry.getValue();
			result.append(key).append("=").append(value);
		}

		while (iterator.hasNext()) {

			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
					.next();
			String key = entry.getKey();
			String value = entry.getValue();

			result.append("&");
			result.append(key).append("=").append(value);
		}

		return result.toString();
	}

}
