package com.netflix.hystrix.entity.exception;

public class InvalidParamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5463045840002891070L;

	private Number id;
	private String message;
	
	public InvalidParamException(Number id, String message) {
		super();
		this.id = id;
		this.message = message;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
