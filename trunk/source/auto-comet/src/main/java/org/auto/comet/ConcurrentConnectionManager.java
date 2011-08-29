package org.auto.comet;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该类用于处理各种通信请求，并管理socket
 * 
 * @author XiaohangHu
 * */
public class ConcurrentConnectionManager extends AbstractConnectionManager {

	/** rwLock为SocketManager的锁 */
	private ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private Lock readLock = rwLock.readLock();
	private Lock writeLock = rwLock.writeLock();

	public ConcurrentConnectionManager() {
		super();
	}

	public ConcurrentConnectionManager(long pushTimeout) {
		super(pushTimeout);
	}

	/**
	 * 检查超时
	 * */
	public void checkPushTimeout() {
		super.checkPushTimeout();
	}

	/**
	 * 创建新链接
	 * */
	public void creatConnection(PushSocket socket) {
		writeLock.lock();
		try {
			super.creatConnection(socket);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 断开链接
	 * */
	public void disconnect(String connectionId, SocketHandler handler,
			HttpServletRequest request) {
		writeLock.lock();
		try {
			super.disconnect(connectionId, handler, request);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 接收消息
	 * */
	public void receiveMessage(String connectionId, HttpServletRequest request,
			HttpServletResponse response) {
		readLock.lock();
		try {
			super.receiveMessage(connectionId, request, response);
		} finally {
			readLock.unlock();
		}
	}

	public PushSocket removeSocket(PushSocket socket) {
		return super.removeSocket(socket);
	}

	@Override
	public PushSocket removeSocket(Serializable id) {
		writeLock.lock();
		try {
			return super.socketStore.remove(id);
		} finally {
			writeLock.unlock();
		}
	}

}
