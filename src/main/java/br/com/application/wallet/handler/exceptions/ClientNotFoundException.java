package br.com.application.wallet.handler.exceptions;

/**
 * Essa exceção é lançada quando a busca por um cliente retorna Optional.empty() ou null.
 */
public class ClientNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8045587788801138699L;

	public ClientNotFoundException(String message) {
		super(message);
	}

	public ClientNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
