package org.auto.comet.test.ning;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.Response;

/**
 * @author xiaohanghu
 */
public class NinghttpClient {

	public static void main(String[] args) throws Exception {
		String url = "http://www.ning.com/";
		url = "http://www.sohu.com/";

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		BoundRequestBuilder boundRequestBuilder = asyncHttpClient
				.prepareGet(url);

		boundRequestBuilder.execute(new AsyncCompletionHandler<Response>() {

			@Override
			public Response onCompleted(Response response) throws Exception {
				System.out.println(response.getHeaders());
				// System.out.println(response.getResponseBody("GBK"));
				System.out.println(Thread.currentThread());
				return response;
			}

			@Override
			public void onThrowable(Throwable t) {
				// Something wrong happened.
			}
		});
		System.out.println(Thread.currentThread());
		// asyncHttpClient.close();

	}

}
