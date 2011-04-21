package org.auto.comet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XiaohangHu
 * */
public interface SocketHandler {

	void accept(Socket socket, HttpServletRequest request);

	void quit(Socket socket, HttpServletRequest request);

}
