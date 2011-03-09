package org.auto.comet.web;

public class DispatchRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4324289825948938207L;

	public DispatchRuntimeException() {
		super();
	}

	public DispatchRuntimeException(String message) {
		super(message);
	}

	public DispatchRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public DispatchRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}