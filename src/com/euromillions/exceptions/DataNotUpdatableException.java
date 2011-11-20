package com.euromillions.exceptions;


public class DataNotUpdatableException extends Exception {

	private static final long serialVersionUID = -276912359661421140L;
	
	private String message = "Data is not updatable";
	
	@Override
	public String getMessage() {
		return this.message;
	}	

}
