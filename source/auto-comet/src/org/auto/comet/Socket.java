package org.auto.comet;

/**
 * PushSocket：单向推送消息
 *
 * @author XiaohangHu
 * */
public interface Socket {

	/**
	 * 将消息用指定的writer发送
	 * */
	void sendMessage(String message) throws PushRuntimeException;

	/**
	 * 设置错误处理器
	 * */
	void setErrorHandler(ErrorHandler errorHandler);

}
