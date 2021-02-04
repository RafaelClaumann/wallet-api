package br.com.application.wallet.handler;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.ClientOpenedExpensesException;
import br.com.application.wallet.handler.messages.ApiError;
import br.com.application.wallet.handler.messages.FieldMessage;
import br.com.application.wallet.handler.messages.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(value = ClientNotFoundException.class)
	public ResponseEntity<Object> handleClientNotFoundException(ClientNotFoundException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.NOT_FOUND,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = ClientOpenedExpensesException.class)
	public ResponseEntity<Object> handleClientOpenedExpensesException(ClientOpenedExpensesException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.BAD_REQUEST,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException exception) {
		final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		final String message = "Erro de Validação";
		final LocalDateTime date = LocalDateTime.now();
		ValidationError validationError = new ValidationError(exception.getClass().getCanonicalName(), badRequest,
				message, date);

		exception.getBindingResult().getFieldErrors().forEach(
				error -> validationError.addError(new FieldMessage(error.getField(), error.getDefaultMessage())));

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}

}
