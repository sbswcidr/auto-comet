package org.auto.comet;

import java.util.List;

import org.auto.comet.listener.SocketListener;

/**
 * PushSocket：单向推送消息
 *
 * 该接口提供给controller
 *
 * @author XiaohangHu
 * */
public interface Socket {

	/**
	 * 发送消息
	 *
	 */
	void sendMessage(String message) throws PushException;

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

	/**
	 * 获得用户缓存的消息
	 *
	 * 当发生异常时，可以尝试获取没有发送成功的消息
	 *
	 */
	List<String> getUserMessages();

}