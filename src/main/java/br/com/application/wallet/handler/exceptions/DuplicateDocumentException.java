package br.com.application.wallet.handler.exceptions;

/**
 * Essa exceção é lançada durante a tentativa do cadastro de um CPF que já esteja no banco de dados.
 */
public class DuplicateDocumentException extends RuntimeException {
	private static final long serialVersionUID = -1600773296089225724L;

	public DuplicateDocumentException(String message) {
		super(message);
	}

	public DuplicateDocumentException(String message, Throwable cause) {
		super(message, cause);
	}
}
