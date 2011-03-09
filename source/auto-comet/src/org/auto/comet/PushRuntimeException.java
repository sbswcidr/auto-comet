package org.auto.comet;

/**
 * @author XiaohangHu
 * */
public class PushRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public PushRuntimeException() {
		super();
	}

	public PushRuntimeException(String message) {
		super(message);
	}

	public PushRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public PushRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
