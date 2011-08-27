package org.auto.comet;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auto.comet.listener.SocketEvent;
import org.auto.comet.listener.SocketListener;
import org.auto.comet.support.SocketStore;
import org.auto.comet.web.DispatchException;

/**
 * 该类用于处理各种通信请求，并管理socket
 * 
 * @author XiaohangHu
 * */
public class SocketManager {

	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** 默认为1分钟 */
	private long pushTimeout = 60000l;

	/** 异步超时时间，默认一小时 */
	private long asyncTimeout = 3600000;

	private SocketStore socketStore;

	/** rwLock为SocketManager的锁 */
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private Lock readLock = rwLock.readLock();
	private Lock writeLock = rwLock.writeLock();

	private SocketListener socketListener = new SocketListener() {
		@Override
		public void onReallyClose(SocketEvent event) {
			PushSocket socket = event.getPushSocket();
			removeSocket(socket);
		}
	};

	private SecureRandom random;
	{
		try {
			random = SecureRandom.getInstance(RANDOM_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Can't find secure random["
					+ RANDOM_ALGORITHM + "]", e);
		}
	}

	public SocketManager(SocketStore socketStore) {
		this.socketStore = socketStore;
	}

	/**
	 * 开始定时任务
	 * */
	public void startTimer() {
		Timer timer = new Timer(true);
		long period = pushTimeout / 2l;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 异常处理，防止守护线程死亡！
				try {
					processPushTimeout();
				} catch (Throwable e) {
					logger.error("Push timeout Exception!", e);
				}
			}
		}, pushTimeout, period);
	}

	public long getPushTimeout() {
		return pushTimeout;
	}

	public void setPushTimeout(long pushTimeout) {
		this.pushTimeout = pushTimeout;
	}

	public long getAsyncTimeout() {
		return asyncTimeout;
	}

	public void setAsyncTimeout(long asyncTimeout) {
		this.asyncTimeout = asyncTimeout;
	}

	private void processPushTimeOut(PushSocket socket) {
		boolean timetOut = socket.processPushTimeOut(this.pushTimeout);
		if (timetOut) {// 超时将socket移除
			this.removeSocket(socket);
		}
	}

	/**
	 * 推送超时处理
	 * */
	private void processPushTimeout() {
		readLock.lock();
		try {
			Collection<PushSocket> sockets = this.socketStore.getAllSocket();
			for (PushSocket socket : sockets) {// 检查所有的socket是否超时
				processPushTimeOut(socket);
			}
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * 接收消息
	 * */
	public void receiveMessage(String connectionId, HttpServletRequest request,
			HttpServletResponse response) {
		PushSocket socket = this.getSocket(connectionId);
		if (null == socket) {
			throw new PushException("Cant't find socket！");
		}
		socket.receiveRequest(request, response);
	}

	/**
	 * 创建新链接
	 * */
	public void creatConnection(PushSocket socket) {
		writeLock.lock();
		try {
			String id = this.getConnectionId();
			socket.setId(id);
			socket.addListener(socketListener);
			socket.setTimeout(this.asyncTimeout);
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("creatConnection [" + id + "]");
			}
			this.addSocket(socket);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 断开链接
	 * */
	public void disconnect(String connectionId, SocketHandler handler,
			HttpServletRequest request) {
		if (null == connectionId) {
			throw new IllegalArgumentException(
					"Disconnect. ConnectionId must not be null!");
		}
		if (null == handler) {
			throw new DispatchException("Cant't find handler");
		}
		PushSocket socket = getSocket(connectionId);
		if (null == socket) {
			if (this.logger.isWarnEnabled()) {
				this.logger
						.warn("Disconnect. Can't find socket by connectionId ["
								+ connectionId + "]");
			}
			return;
		}
		handler.quit(socket, request);
		socket.close();
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("disconnect [" + connectionId + "]");
		}
	}

	private String getRandom() {
		long l = random.nextLong();
		l = l >= 0L ? l : -l;
		return Long.toString(l, 36);
	}

	/**
	 * 返回一个不重复的安全随机id
	 * */
	private String getConnectionId() {
		String id = getRandom();
		while (hasSocket(id)) {
			id = getRandom();
		}
		return id;
	}

	private PushSocket getSocket(Serializable id) {
		return socketStore.getSocket(id);
	}

	private PushSocket removeSocket(PushSocket socket) {
		Serializable id = socket.getId();
		return removeSocket(id);
	}

	private PushSocket removeSocket(Serializable id) {
		return socketStore.removeSocket(id);
	}

	private void addSocket(PushSocket socket) {
		socketStore.addSocket(socket);
	};

	private boolean hasSocket(Serializable id) {
		return socketStore.hasSocket(id);
	}
}
