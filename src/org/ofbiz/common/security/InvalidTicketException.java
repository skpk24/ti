package org.ofbiz.common.security;

public class InvalidTicketException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3011261615842747658L;

	public InvalidTicketException(String message) {
		super(message);
	}
	
	public InvalidTicketException(String message, Throwable exception){
		super(message, exception);
	}
	
	
	
}
