package org.auto.comet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConcurrentPushSocket extends AbstractPushSocket {
	
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

	public synchronized boolean processPushTimeOut(long pushTimeout) {
		return super.processPushTimeOut(pushTimeout);
	}
}
