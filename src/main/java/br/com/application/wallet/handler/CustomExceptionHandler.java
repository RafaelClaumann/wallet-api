package br.com.application.wallet.handler;

import br.com.application.wallet.handler.exceptions.ClientNotFoundException;
import br.com.application.wallet.handler.exceptions.OpenedExpensesException;
import br.com.application.wallet.handler.exceptions.DuplicateDocumentException;
import br.com.application.wallet.handler.exceptions.WalletNotFoundException;
import br.com.application.wallet.handler.messages.ApiError;
import br.com.application.wallet.handler.messages.FieldMessage;
import br.com.application.wallet.handler.messages.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(value = ClientNotFoundException.class)
	public ResponseEntity<ApiError> handleClientNotFoundException(ClientNotFoundException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.NOT_FOUND,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = WalletNotFoundException.class)
	public ResponseEntity<ApiError> handleWalletNotFoundException(WalletNotFoundException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.NOT_FOUND,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = OpenedExpensesException.class)
	public ResponseEntity<ApiError> handleOpenedExpensesException(OpenedExpensesException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.BAD_REQUEST,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> handleArgumentNotValidException(MethodArgumentNotValidException exception) {
		final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		final String message = "Erro de Validação";

		ValidationError validationError = new ValidationError(exception.getClass().getCanonicalName(), badRequest,
				message, LocalDateTime.now());

		exception.getBindingResult().getFieldErrors().forEach(
				error -> validationError.addError(new FieldMessage(error.getField(), error.getDefaultMessage())));

		return new ResponseEntity<>(validationError, badRequest);
	}

	@ExceptionHandler(value = DuplicateDocumentException.class)
	public ResponseEntity<ApiError> handleDuplicateDocumentException(DuplicateDocumentException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.BAD_REQUEST,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	// Handle POST empty body
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleHttpMessageNotReadableExceptionException(HttpMessageNotReadableException exception) {
		ApiError exceptionMessage = new ApiError(exception.getClass().getCanonicalName(), HttpStatus.UNPROCESSABLE_ENTITY,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(exceptionMessage, HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
