package br.com.application.wallet.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiExceptionMessage {

	private final HttpStatus status;
	private final String message;
	private final LocalDateTime localDateTime;

	public ApiExceptionMessage(HttpStatus status, String message, LocalDateTime localDateTime) {
		this.status = status;
		this.message = message;
		this.localDateTime = localDateTime;
	}

}
