package br.com.application.wallet.handler.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FieldMessage {

	@JsonProperty("field_name")
	private final String fieldName;

	@JsonProperty("error_message")
	private final String errorMessage;

	public FieldMessage(String field, String message) {
		this.fieldName = field;
		this.errorMessage = message;
	}
}
