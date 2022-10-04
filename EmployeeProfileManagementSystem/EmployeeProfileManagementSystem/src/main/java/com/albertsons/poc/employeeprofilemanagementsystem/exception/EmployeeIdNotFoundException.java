package com.albertsons.poc.employeeprofilemanagementsystem.exception;

public class EmployeeIdNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public EmployeeIdNotFoundException(String message) {
		super();
		this.message = message;
	}

	public EmployeeIdNotFoundException() {
		super();
	}

}
