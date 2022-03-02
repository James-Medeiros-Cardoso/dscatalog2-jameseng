package com.jameseng.dscatalog2.services.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jameseng.dscatalog2.resources.exceptions.StandardError;

@ControllerAdvice // intercepta exceções na camada de resource e trata as exceções
public class ResourceExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class) // EntityNotFoundException = exceção que será interceptada
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {

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

}
