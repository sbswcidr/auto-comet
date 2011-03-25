package org.auto.comet;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author XiaohangHu
 * */
public interface SocketStore {

	PushSocket getSocket(Serializable id);

	PushSocket removeSocket(Serializable id);

	void addSocket(PushSocket socket);

	boolean hasSocket(Serializable id);

	Collection<PushSocket> getAllSocket();

}
