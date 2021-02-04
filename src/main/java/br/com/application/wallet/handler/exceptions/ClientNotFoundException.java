package br.com.application.wallet.handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ClientNotFoundException extends RuntimeException {

	public ClientNotFoundException(String message) {
		super(message);
	}

	public ClientNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
