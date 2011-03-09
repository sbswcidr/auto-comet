package org.auto.comet.service;

import org.auto.comet.web.Socket;
import org.auto.comet.web.RequestParameter;

/**
 * @author XiaohangHu
 * */
public interface CometService {

	void accept(Socket socket, RequestParameter requestParameter);

	void quit(Socket socket);

}
