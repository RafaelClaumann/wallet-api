package br.com.application.wallet.handler.exceptions;

/**
 * Essa exceção é lançada quando tentamos deletar um cliente que possui despesas em aberto.
 */
public class ClientOpenedExpensesException extends RuntimeException{
	private static final long serialVersionUID = 4890240452277672520L;

	public ClientOpenedExpensesException(String message) {
		super(message);
	}

	public ClientOpenedExpensesException(String message, Throwable cause) {
		super(message, cause);
	}
}
