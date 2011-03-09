package org.auto.comet.web;

import org.auto.comet.PushRuntimeException;

/**
 * PushSocket：单向推送消息
 *
 * @author XiaohangHu
 * */
public interface Socket {

	/**
	 * 将消息用指定的writer发送
	 * */
	public void sendMessage(String message) throws PushRuntimeException;

}
