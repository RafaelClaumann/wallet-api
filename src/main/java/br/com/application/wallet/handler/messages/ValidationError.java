package br.com.application.wallet.handler.messages;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends ApiError {

	private final List<FieldMessage> errors = new ArrayList<>();

	public ValidationError(String exceptionClass, HttpStatus status, String message, LocalDateTime localDateTime) {
		super(exceptionClass, status, message, localDateTime);
	}

	public void addError(FieldMessage error) {
		this.errors.add(error);
	}
}
