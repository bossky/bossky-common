package org.bossky.exception;

/**
 * 系统忙异常
 * 
 * @author bo
 *
 */
public class BusyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusyException() {
		super();
	}

	public BusyException(String message) {
		super(message);
	}

	public BusyException(String message, Throwable e) {
		super(message, e);
	}
}
