package org.auto.comet;

import org.auto.comet.listener.SocketListener;

/**
 * PushSocket：单向推送消息
 *
 * @author XiaohangHu
 * */
public interface Socket {

	/**
	 * 发送消息
	 *
	 */
	void sendMessage(String message) throws PushRuntimeException;

	/**
	 * 关闭连接
	 *
	 */
	void close();

	/**
	 * 设置错误处理器
	 *
	 */
	void setErrorHandler(ErrorHandler errorHandler);

	/**
	 * 添加监听器
	 *
	 */
	void addListener(SocketListener listener);

}
