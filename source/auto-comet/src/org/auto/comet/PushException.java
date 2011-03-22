package org.auto.comet;

/**
 * @author XiaohangHu
 * */
public class PushException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public PushException() {
		super();
	}

	public PushException(String message) {
		super(message);
	}

	public PushException(Throwable throwable) {
		super(throwable);
	}

	public PushException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
