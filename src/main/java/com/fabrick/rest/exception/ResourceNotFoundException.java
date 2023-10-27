package com.fabrick.rest.exception;

public class ResourceNotFoundException extends Exception {
   
	private static final long serialVersionUID = -4537114245395950947L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}