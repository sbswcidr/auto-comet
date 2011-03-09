package org.auto.comet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auto.comet.PushRuntimeException;
import org.auto.comet.web.listener.AsyncAdapter;
import org.auto.comet.web.listener.SocketEvent;
import org.auto.comet.web.listener.SocketListener;
import org.auto.json.JsonArray;
import org.auto.json.JsonObject;

/**
 * @author XiaohangHu
 * */
public class PushSocket implements Socket {
	private Serializable id;

	private static final String CLOSE_MESSAGE;

	private List<String> messages;

	private boolean close = false;

	private AsyncContext asyncContext;

	private List<SocketListener> listeners = new LinkedList<SocketListener>();

	static {
		JsonObject commend = new JsonObject();
		commend.put(SocketDispatcherServlet.SYNCHRONIZE_KEY,
				SocketDispatcherServlet.DISCONNECT_VALUE);
		CLOSE_MESSAGE = commend.toString();
	}
	{
		messages = new LinkedList<String>();
	}

	public PushSocket(Serializable id) {
		this.id = id;
	}

	Serializable getId() {
		return id;
	}

	/**
	 * 是否有消息要发送
	 * */
	private boolean hasMessage() {
		return !this.messages.isEmpty();
	}

	/**
	 * 异步等待消息
	 * */
	private void waitMessage(HttpServletRequest request) {
		try {
			AsyncContext ac = request.startAsync();
			ac.setTimeout(3600000);// 一小时
			ac.addListener(new AsyncAdapter() {
				@Override
				public void onComplete(AsyncEvent asyncevent)
						throws IOException {
					// TODO Auto-generated method stub
				}

				@Override
				public void onError(AsyncEvent asyncevent) throws IOException {
					// TODO Auto-generated method stub
				}

				@Override
				public void onStartAsync(AsyncEvent asyncevent)
						throws IOException {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTimeout(AsyncEvent asyncevent) throws IOException {
					close();
				}
			});
			this.asyncContext = ac;
		} catch (Throwable e) {
			throw new PushRuntimeException(e);
		}
	}

	private void sendMessage(List<String> messages, ServletResponse response)
			throws IOException {
		sendMessage(messages, response.getWriter());
	}

	private void sendMessage(String message, ServletResponse response)
			throws IOException {
		List<String> msgs = new LinkedList<String>();
		msgs.add(message);
		sendMessage(msgs, response);
	}

	/**
	 * 将消息用指定的writer发送
	 * */
	private void sendMessage(List<String> messages, PrintWriter writer) {
		JsonArray array = new JsonArray();
		for (String message : messages) {
			if (isCloseMessage(message)) {
				// 发送关闭消息才真正关闭链接,停止轮询
				fireReallyClose();
			}
			array.add(message);
		}
		writer.write(array.toString());
		writer.flush();
	}

	private boolean isCloseMessage(String msg) {
		// 用==提高比较效率
		return CLOSE_MESSAGE == msg;
	}

	/**
	 * 是在等待
	 * */
	private boolean isWaiting() {
		return null != this.asyncContext;
	}

	/**
	 * 接待取消息请求
	 *
	 * @throws IOException
	 * */
	synchronized void receiveRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (this.hasMessage()) {
			// 如果有消息则直接将消息发送
			sendMessage(this.messages, response);
			// 发送后清空缓冲区
			this.messages.clear();
		} else {
			// 如果没有消息则等待消息
			this.waitMessage(request);
		}
	}

	public synchronized void sendMessage(String message)
			throws PushRuntimeException{
		if (isClosed()) {
			throw new PushRuntimeException("链接已经关闭！");
		}
		// 如果不是等待状态，将消息缓存
		if (!isWaiting()) {
			this.messages.add(message);
			return;
		}
		// 如果是等待状态，发送消息
		ServletResponse response = this.asyncContext.getResponse();
		try {
			sendMessage(message, response);
		} catch (IOException e) {
			throw new PushRuntimeException("IOException push message", e);
		}
		complete();
	}

	private void complete() {
		this.asyncContext.complete();
		this.asyncContext = null;
	}

	public boolean isClosed() {
		return close;
	}

	public synchronized void close() {
		this.sendMessage(CLOSE_MESSAGE);
		this.close = true;
	}

	/**
	 * 触发ReallyClose事件
	 * */
	protected void fireReallyClose() {
		SocketEvent event = new SocketEvent(this);
		for (SocketListener listener : listeners) {
			listener.onReallyClose(event);
		}
	}

	public void addListener(SocketListener listener) {
		listeners.add(listener);
	}

}
