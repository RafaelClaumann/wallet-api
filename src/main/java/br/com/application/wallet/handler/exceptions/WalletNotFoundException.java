package br.com.application.wallet.handler.exceptions;

/**
 * Essa exceção é lançada quando a busca por uma carteira retorna Optional.empty() ou null.
 */
public class WalletNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8045587788801138699L;

	public WalletNotFoundException(String message) {
		super(message);
	}

	public WalletNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
