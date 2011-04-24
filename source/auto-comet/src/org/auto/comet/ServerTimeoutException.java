package org.auto.comet;

/**
 * @author XiaohangHu
 * */
public class ServerTimeoutException extends PushTimeoutException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public ServerTimeoutException() {
		super();
	}

	public ServerTimeoutException(String message) {
		super(message);
	}

	public ServerTimeoutException(Throwable throwable) {
		super(throwable);
	}

	public ServerTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
