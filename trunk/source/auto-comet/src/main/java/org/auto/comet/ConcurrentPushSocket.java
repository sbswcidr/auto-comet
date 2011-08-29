package org.auto.comet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConcurrentPushSocket extends AbstractPushSocket {
	/**
	 * 接待取消息请求
	 * 
	 * @throws IOException
	 * */
	public synchronized void receiveRequest(HttpServletRequest request,
			HttpServletResponse response) {
		super.receiveRequest(request, response);
	}

	public synchronized void send(String message) {
		super.send(message);
	}

	/**
	 * 关闭连接
	 * */
	public synchronized void close() {
		super.close();
	}

	/**
	 * 处理推送超时，超时推送代表客户端长时间没有发送连接请求
	 * 
	 * 超时会发生一个连接异常。
	 * 
	 * @param pushTimeout
	 *            超时时间
	 * @return 是否超时
	 */
	public synchronized boolean processPushTimeOut(long pushTimeout) {
		return super.processPushTimeOut(pushTimeout);
	}
}
