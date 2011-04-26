package org.auto.comet.example.chat.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.auto.comet.ErrorHandler;
import org.auto.comet.PushException;
import org.auto.comet.Socket;
import org.auto.comet.SocketHandler;
import org.auto.comet.example.chat.service.impl.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author XiaohangHu
 * */
@Controller
public class ChatRoomSocketHandler implements SocketHandler {

	@Autowired
	private ChatRoomService chatRoomService;

	private Map<Serializable, Socket> socketMapping = new HashMap<Serializable, Socket>();

	/** rwLock为ChatRoomSocketHandler的锁 */
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private Lock readLock = rwLock.readLock();
	private Lock writeLock = rwLock.writeLock();

	@Override
	public boolean accept(Socket socket, HttpServletRequest request) {
		String userId = request.getParameter("userId");

		if (socketMapping.get(userId) != null) {
			return false;
		}
		socket.setErrorHandler(new ErrorHandler() {
			@Override
			public void error(Socket socket, PushException e) {
				romveSocket(socket);
				throw e;
			}
		});
		writeLock.lock();
		try {
			socketMapping.put(userId, socket);
		} finally {
			writeLock.unlock();
		}
		return true;
		// return false;
	}

	@Override
	public void quit(Socket socket, HttpServletRequest request) {
		String userId = request.getParameter("userId");
		writeLock.lock();
		try {
			socketMapping.remove(userId);
			this.chatRoomService.loginOut(userId);
		} finally {
			writeLock.unlock();
		}
	}

	private void romveSocket(Socket socket) {
		writeLock.lock();
		try {
			for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
				Socket value = entry.getValue();
				if (value.equals(socket)) {
					Serializable key = entry.getKey();
					socketMapping.remove(key);
				}
			}
		} finally {
			writeLock.unlock();
		}
	}

	public void sendMessage(Serializable id, String msg) {
		readLock.lock();
		try {
			Socket socket = this.socketMapping.get(id);
			if (null != socket) {
				socket.send(msg);
			} else {
				throw new RuntimeException("Can't find socket!");
			}
		} finally {
			readLock.unlock();
		}
	}

	public void sendMessageExcept(Serializable exceptId, String msg) {
		readLock.lock();
		try {
			for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
				Serializable id = entry.getKey();
				Socket socket = entry.getValue();
				if (id.equals(exceptId)) {
					continue;
				}
				socket.send(msg);
			}
		} finally {
			readLock.unlock();
		}
	}

	public void sendMessageAll(String msg) {
		readLock.lock();
		try {
			for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
				Socket socket = entry.getValue();
				socket.send(msg);
			}
		} finally {
			readLock.unlock();
		}
	}

	public Set<Serializable> getAllId() {
		Set<Serializable> result = new HashSet<Serializable>();
		readLock.lock();
		try {
			for (Entry<Serializable, Socket> entry : socketMapping.entrySet()) {
				result.add(entry.getKey());
			}
			return result;
		} finally {
			readLock.unlock();
		}

	}

	public synchronized boolean hasId(Serializable id) {
		return null != this.socketMapping.get(id);
	}
}
