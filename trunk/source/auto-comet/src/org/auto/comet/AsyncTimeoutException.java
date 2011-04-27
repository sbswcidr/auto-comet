package org.auto.comet;

/**
 * @author XiaohangHu
 * */
public class AsyncTimeoutException extends PushException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public AsyncTimeoutException() {
		super();
	}

	public AsyncTimeoutException(String message) {
		super(message);
	}

	public AsyncTimeoutException(Throwable throwable) {
		super(throwable);
	}

	public AsyncTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
