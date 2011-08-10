package org.auto.comet.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.auto.comet.PushSocket;
import org.auto.comet.Socket;

/**
 * @author XiaohangHu
 * */
public class LocalSocketStore implements SocketStore {

	private Map<Serializable, PushSocket> sockets;
	{
		this.sockets = new HashMap<Serializable, PushSocket>();
	}

	public PushSocket getSocket(Serializable id) {
		return this.sockets.get(id);
	}

	public PushSocket removeSocket(Serializable id) {
		return this.sockets.remove(id);
	}

	public void addSocket(PushSocket socket) {
		this.sockets.put(socket.getId(), socket);
	};

	public boolean hasSocket(Serializable id) {
		Socket socket = this.getSocket(id);
		return null != socket;
	}

	public Collection<PushSocket> getAllSocket() {
		return this.sockets.values();
	}

}
