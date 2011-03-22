package org.auto.comet.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.auto.comet.Socket;

/**
 * @author XiaohangHu
 * */
public interface SocketController {

	void accept(Socket socket, HttpServletRequest request);

	void quit(Socket socket, HttpServletRequest request);

}
