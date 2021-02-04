package br.com.application.wallet.handler.messages;

import lombok.Getter;

@Getter
public class FieldMessage {

	private final String fieldName;
	private final String errorMessage;

	public FieldMessage(String field, String message) {
		this.fieldName = field;
		this.errorMessage = message;
	}
}
