package org.auto.comet.listener;

import java.util.EventListener;

/**
 * @author XiaohangHu
 * */
public interface SocketListener extends EventListener {
	/**
	 * 当真正关闭链接时触发
	 * */
	void onReallyClose(SocketEvent event);
}
