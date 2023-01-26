package com.axlebank.exception;

public class InstitutionNotFoundException extends Exception {
	
	public static final long serialVersionUID = 1L;
	
	public InstitutionNotFoundException(String msg) {
		super(msg);
	}

}
