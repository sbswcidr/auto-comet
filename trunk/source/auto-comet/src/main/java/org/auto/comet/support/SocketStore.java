package org.auto.comet.support;

import java.io.Serializable;
import java.util.Collection;

import org.auto.comet.PushSocket;

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
