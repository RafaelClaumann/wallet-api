package br.com.application.wallet.handler.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * ApiError.java
 * Encapsula uma mensagem atraves de seus atributos.
 */
@Getter
public class ApiError {

	@JsonProperty("exception_name")
	private final String exceptionName;

	@JsonProperty("http_status_code")
	private final HttpStatus status;

	@JsonProperty("exception_message")
	private final String message;

	@JsonProperty("local_date_time")
	private final LocalDateTime localDateTime;

	public ApiError(String exceptionName, HttpStatus status, String message, LocalDateTime localDateTime) {
		this.exceptionName = exceptionName;
		this.status = status;
		this.message = message;
		this.localDateTime = localDateTime;
	}

}
