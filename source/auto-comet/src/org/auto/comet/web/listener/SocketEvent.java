package org.auto.comet.web.listener;

import org.auto.comet.web.PushSocket;

/**
 *
 * @author XiaohangHu
 * */
public class SocketEvent {

	private PushSocket socket;

	public SocketEvent(PushSocket socket) {
		this.socket = socket;
	}

	public PushSocket getPushSocket() {
		return this.socket;
	}
}
