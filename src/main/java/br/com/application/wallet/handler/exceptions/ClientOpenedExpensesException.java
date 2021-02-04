package br.com.application.wallet.handler.exceptions;

public class ClientOpenedExpensesException extends RuntimeException{

	public ClientOpenedExpensesException(String message) {
		super(message);
	}

	public ClientOpenedExpensesException(String message, Throwable cause) {
		super(message, cause);
	}
}
