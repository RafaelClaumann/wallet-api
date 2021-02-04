package br.com.application.wallet.handler;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.ClientOpenedExpensesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(value = ClientNotFoundException.class)
	public ResponseEntity<Object> handleClientNotFoundException(ClientNotFoundException exception) {
		ApiExceptionMessage exceptionMessage = new ApiExceptionMessage(HttpStatus.NOT_FOUND, exception.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = ClientOpenedExpensesException.class)
	public ResponseEntity<Object> handleClientOpenedExpensesException(ClientOpenedExpensesException exception) {
		ApiExceptionMessage exceptionMessage = new ApiExceptionMessage(HttpStatus.BAD_REQUEST, exception.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}
}
