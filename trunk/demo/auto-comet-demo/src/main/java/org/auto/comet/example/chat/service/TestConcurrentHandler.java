package org.auto.comet.example.chat.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.AsyncTimeoutException;
import org.auto.comet.ErrorHandler;
import org.auto.comet.PushException;
import org.auto.comet.PushTimeoutException;
import org.auto.comet.Socket;
import org.auto.comet.SocketHandler;
import org.springframework.stereotype.Controller;

/**
 * 并发测试
 *
 * @author XiaohangHu
 * */
@Controller
public class TestConcurrentHandler implements SocketHandler {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

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
				if (e instanceof AsyncTimeoutException) {
					logger.debug("AsyncTimeoutException", e);
				} else if (e instanceof PushTimeoutException) {
					logger.debug("PushTimeoutException", e);
				} else {
					logger.error("PushException!", e);
				}
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

	public void send(Serializable id, String msg) {
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

	public void sendExcept(Serializable exceptId, String msg) {
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

	public void sendToAll(String msg) {
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
		readLock.lock();
		try {
			return null != this.socketMapping.get(id);
		} finally {
			readLock.unlock();
		}
	}
}
