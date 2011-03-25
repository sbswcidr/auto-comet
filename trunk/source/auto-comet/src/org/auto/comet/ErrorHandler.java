package org.auto.comet;

/**
 * SocketErrorHandler
 *
 * 通信错误处理接口
 *
 * @author XiaohangHu
 * */
public interface ErrorHandler {

	public void error(Socket socket, Exception e);

}
