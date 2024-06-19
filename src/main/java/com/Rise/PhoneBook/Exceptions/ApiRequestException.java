package com.Rise.PhoneBook.Exceptions;

public class ApiRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApiRequestException(String mesage) {
		super(mesage);
	}

	public static String NotFoundException(String id) {
		return "Contact with id : " + id + " not found";

	}

	public ApiRequestException(String mesage, Throwable cause) {
		super(mesage, cause);
	}
}
