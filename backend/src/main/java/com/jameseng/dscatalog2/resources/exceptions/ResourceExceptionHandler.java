package com.jameseng.dscatalog2.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jameseng.dscatalog2.services.exceptions.DatabaseException;
import com.jameseng.dscatalog2.services.exceptions.ResourceNotFoundException;

@ControllerAdvice // intercepta exceções na camada de resource e trata as exceções
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class) // EntityNotFoundException = exceção que será interceptada
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {

		// HttpServletRequest = tem informações da requisição

		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError err = new StandardError();

		err.setTimestamp(Instant.now()); // Instant.now() = pega o horário atual
		err.setStatus(status.value()); // NOT_FOUND = código 404
		err.setError("Resource not Found");
		err.setMessage(e.getMessage()); // getMessage() = pega a mensagem definida no service
		err.setPath(request.getRequestURI()); // path da requisição

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DatabaseException.class) // EntityNotFoundException = exceção que será interceptada
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {

		// HttpServletRequest = tem informações da requisição

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError();

		err.setTimestamp(Instant.now()); // Instant.now() = pega o horário atual
		err.setStatus(status.value()); // NOT_FOUND = código 404
		err.setError("Database Exception");
		err.setMessage(e.getMessage()); // getMessage() = pega a mensagem definida no service
		err.setPath(request.getRequestURI()); // path da requisição

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class) // MethodArgumentNotValidException = será interceptada
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		// HttpServletRequest = tem informações sobre a requisição

		// UNPROCESSABLE_ENTITY = entidade não processável (422)
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

		ValidationError err = new ValidationError();

		err.setTimestamp(Instant.now()); // Instant.now() = pega o horário atual
		err.setStatus(status.value()); // código 422
		err.setError("Validation Exception");
		err.setMessage(e.getMessage()); // getMessage() = pega a mensagem definida no service
		err.setPath(request.getRequestURI()); // path da requisição
		
		// getBindingResult() = coleta os erros da validação
		// getFieldErrors() = lista de erros da validação
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(err);
	}

}
