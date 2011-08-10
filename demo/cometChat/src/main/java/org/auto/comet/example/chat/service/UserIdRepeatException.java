package org.auto.comet.example.chat.service;

/**
 * @author XiaohangHu
 * */
public class UserIdRepeatException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -1602707905511112833L;

	public UserIdRepeatException() {
		super();
	}

	public UserIdRepeatException(String message) {
		super(message);
	}

	public UserIdRepeatException(Throwable throwable) {
		super(throwable);
	}

	public UserIdRepeatException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
