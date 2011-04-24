package org.auto.comet;

/**
 * @author XiaohangHu
 * */
public class ClientTimeoutException extends PushTimeoutException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4953949710626671131L;

	public ClientTimeoutException() {
		super();
	}

	public ClientTimeoutException(String message) {
		super(message);
	}

	public ClientTimeoutException(Throwable throwable) {
		super(throwable);
	}

	public ClientTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
