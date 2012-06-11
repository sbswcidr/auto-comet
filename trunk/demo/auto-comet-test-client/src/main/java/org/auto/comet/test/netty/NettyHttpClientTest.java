package org.auto.comet.test.netty;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * @author xiaohanghu
 */
public class NettyHttpClientTest {

	public static void main(String[] args) throws Exception {

		String url = "http://www.sohu.com";
		HttpMethod httpMethod = HttpMethod.GET;

		List<Cookie> cookies = new ArrayList<Cookie>();

		URI uri = new URI(url);
		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		String host = uri.getHost() == null ? "localhost" : uri.getHost();
		if (!scheme.equals("http")) {
			throw new Exception("just support http protocol");
		}
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
				httpMethod, uri.toASCIIString());
		request.setHeader(HttpHeaders.Names.HOST, host);
		request.setHeader(HttpHeaders.Names.CONNECTION,
				HttpHeaders.Values.KEEP_ALIVE);
		if (cookies != null && !cookies.isEmpty()) {
			CookieEncoder httpCookieEncoder = new CookieEncoder(false);
			for (Cookie cookie : cookies) {
				httpCookieEncoder.addCookie(cookie);
			}
			request.setHeader(HttpHeaders.Names.COOKIE,
					httpCookieEncoder.encode());
		}

		ClientBootstrap bootstrap = null;
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new HttpClientPipelineFactory());

		uri = new URI(request.getUri());
		int port = uri.getPort() == -1 ? 80 : uri.getPort();
		ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(
				request.getHeader(HttpHeaders.Names.HOST), port));
		channelFuture.addListener(new ConnectOk(request));
		ChannelPipeline channelPipeline = channelFuture.getChannel()
				.getPipeline();
		channelPipeline.addLast("handler", new HttpResponseHandler());

//		channelFuture.addListener(new ChannelFutureListener() {
//
//			@Override
//			public void operationComplete(ChannelFuture channelfuture)
//					throws Exception {
//				// TODO Auto-generated method stub
//
//			}
//		});

		// allChannels.close().awaitUninterruptibly();
		// bootstrap.releaseExternalResources();

	}

}
