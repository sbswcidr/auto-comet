package org.auto.comet;

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

import org.auto.comet.listener.SocketEvent;
import org.auto.comet.listener.SocketListener;
import org.auto.comet.web.listener.AsyncAdapter;
import org.auto.json.JsonArray;

/**
 * PushSocket
 *
 * states:
 *
 * 是否在等待: isWaiting
 *
 * 是否有消息: hasMessage
 *
 * 是否已经关闭: isClosed
 *
 * @author XiaohangHu
 * */
public class PushSocket implements Socket {

	private Serializable id;

	private static final String CLOSE_MESSAGE;

	/**
	 * 消息队列
	 * */
	private List<String> messages;

	/**
	 * 是否已经预关闭
	 * */
	private boolean close = false;

	/**
	 * 记录上一次推送的时间。客户端长时间没有轮询，应该发生一个异常。
	 * */
	private Long lastPushTime;

	private AsyncContext asyncContext;

	private List<SocketListener> listeners = new LinkedList<SocketListener>();

	private ErrorHandler errorHandler;

	static {
		CLOSE_MESSAGE = JsonProtocolUtils.getCloseCommend();
	}
	{
		messages = new LinkedList<String>();
	}

	public PushSocket(Serializable id) {
		this.id = id;
	}

	public Serializable getId() {
		return id;
	}

	private long getNowTimeInMillis() {
		return System.currentTimeMillis();
	}

	/** 添加监听器 */
	public void addListener(SocketListener listener) {
		listeners.add(listener);
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/** 重置最后推送时间 */
	private void resetLastPushTime() {
		this.lastPushTime = getNowTimeInMillis();
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
	public boolean processPushTimeOut(long pushTimeout) {
		Long lastTime = this.getLastPushTime();
		if (this.isWaiting()) {
			return false;
		} else {
			long now = this.getNowTimeInMillis();
			long sent = now - lastTime;
			if (sent > pushTimeout) {
				fireError(new PushException("推送超时"));
				return true;
			}
		}
		return false;
	}

	/** 获取最后一次推送的时间 */
	protected Long getLastPushTime() {
		return lastPushTime;
	}

	/** 异步等待消息 */
	private void waitMessage(HttpServletRequest request) {
		try {
			AsyncContext ac = request.startAsync();
			ac.setTimeout(3600000);// 一小时
			ac.addListener(new AsyncAdapter() {
				@Override
				public void onError(AsyncEvent asyncevent) throws IOException {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTimeout(AsyncEvent asyncevent) throws IOException {
					close();
				}
			});
			this.asyncContext = ac;
		} catch (Exception e) {
			this.fireError(e);
		} catch (Throwable te) {
			throw new PushException(te);
		}
	}

	private boolean isCloseMessage(String msg) {
		// 用==提高比较效率
		return CLOSE_MESSAGE == msg;
	}

	private void complete() {
		this.asyncContext.complete();
		this.asyncContext = null;
	}

	/**
	 * 真正关闭连接
	 * */
	private void reallyClose() {
		fireReallyClose();
	}

	/**
	 * 触发真正关闭连接事件
	 * */
	protected void fireReallyClose() {
		SocketEvent event = new SocketEvent(this);
		for (SocketListener listener : listeners) {
			listener.onReallyClose(event);
		}
	}

	/**
	 * 触发异常处理
	 * */
	protected void fireError(Exception e) {
		ErrorHandler handler = this.getErrorHandler();
		if (null != handler) {
			handler.error(e);
		}
	}

	/** ~~~~~~~~~~~~~~~~~~~~~~~推送消息~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * 将消息用指定的writer发送
	 * */
	private void pushMessage(List<String> messages, PrintWriter writer) {
		JsonArray array = new JsonArray();
		boolean isClose = false;
		for (String message : messages) {
			if (isCloseMessage(message)) {
				isClose = true;
			}
			array.add(message);
		}
		writer.write(array.toString());
		writer.flush();
		// 如果发送的消息中有关闭消息，则真正关闭连接
		if (isClose) {
			reallyClose();
		}
		// 重置最后推送时间
		resetLastPushTime();
	}

	private void pushMessage(List<String> messages, ServletResponse response)
			throws IOException {
		pushMessage(messages, response.getWriter());
	}

	private void pushMessage(String message, ServletResponse response)
			throws IOException {
		List<String> msgs = new LinkedList<String>();
		msgs.add(message);
		pushMessage(msgs, response);
	}

	/** ~~~~~~~~~~~~~~~~~~~~~~~状态获取~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * 是在等待
	 * */
	private boolean isWaiting() {
		return null != this.asyncContext;
	}

	/**
	 * 是否有消息要发送
	 * */
	private boolean hasMessage() {
		return !this.messages.isEmpty();
	}

	/**
	 * 是否已经关闭
	 * */
	public boolean isClosed() {
		return close;
	}

	/** ~~~~~~~~~~~~~~~~~~~~~~~对外线程安全的接口~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * 接待取消息请求
	 *
	 * @throws IOException
	 * */
	public synchronized void receiveRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (this.hasMessage()) {
			// 如果有消息则直接将消息推送
			pushMessage(this.messages, response);
			// 发送后清空缓冲区
			this.messages.clear();
		} else {
			// 如果没有消息则等待消息
			this.waitMessage(request);
		}
	}

	public synchronized void sendMessage(String message) throws PushException {
		if (isClosed()) {
			throw new PushException("连接已经关闭！");
		}
		// 如果不是等待状态，将消息缓存
		if (!isWaiting()) {
			this.messages.add(message);
			return;
		}
		// 如果是等待状态，发送消息
		ServletResponse response = this.asyncContext.getResponse();
		try {
			pushMessage(message, response);
		} catch (IOException e) {
			throw new PushException("IOException push message", e);
		}
		complete();
	}

	/**
	 * 关闭连接
	 * */
	public synchronized void close() {
		this.sendMessage(CLOSE_MESSAGE);
		this.close = true;
	}
}
