package com.fabrick.rest.exception;


public class BadRequestException extends Exception {
	   
	private static final long serialVersionUID = -533160426585301055L;

	public BadRequestException(String message) {
        super(message);
    }
}