package org.auto.comet.test.netty;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class HttpResponseHandler extends SimpleChannelUpstreamHandler {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		Channel ch = e.getChannel();
		ch.close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		System.out.println(Thread.currentThread());
		
		HttpResponse response = (HttpResponse) e.getMessage();
		System.out.println("STATUS: " + response.getStatus());
		System.out.println("VERSION: " + response.getProtocolVersion());
		System.out.println();
		if (!response.getHeaderNames().isEmpty()) {
			for (String name : response.getHeaderNames()) {
				for (String value : response.getHeaders(name)) {
					System.out.println("HEADER: " + name + " = " + value);
				}
			}
			System.out.println();
		}
		ChannelBuffer content = response.getContent();
		if (content.readable()) {
			System.out.println("CONTENT {");
			System.out.println("len : " + response.getContentLength());
			// System.out.println(content.toString("GBK"));
			System.out.println("} END OF CONTENT");
			//System.out.println(content.toString(content.readerIndex(),
			//		content.readableBytes(), Charset.forName("GBK")));
		}
	}
}