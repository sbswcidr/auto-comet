package org.auto.comet;

/**
 * @author XiaohangHu
 * */
public class PushTimeoutException extends PushException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public PushTimeoutException() {
		super();
	}

	public PushTimeoutException(String message) {
		super(message);
	}

	public PushTimeoutException(Throwable throwable) {
		super(throwable);
	}

	public PushTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
