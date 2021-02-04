package br.com.application.wallet.handler.messages;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiError {

	private final String exceptionName;
	private final HttpStatus status;
	private final String message;
	private final LocalDateTime localDateTime;

	public ApiError(String exceptionName, HttpStatus status, String message, LocalDateTime localDateTime) {
		this.exceptionName = exceptionName;
		this.status = status;
		this.message = message;
		this.localDateTime = localDateTime;
	}

}
