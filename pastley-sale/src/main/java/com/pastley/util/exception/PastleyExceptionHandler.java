package com.pastley.util.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @project Pastley-Sale.
 * @author Sergio Stives Barrios Buitrago.
 * @Github https://github.com/SerBuitrago.
 * @contributors soleimygomez, leynerjoseoa, jhonatanbeltran.
 * @version 1.0.0.
 */
@ControllerAdvice
public class PastleyExceptionHandler {

	private static final Map<String, Integer> STATUS = new HashMap<>();
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@ExceptionHandler(PastleyException.class)
	public final ResponseEntity<PastleExceptionModel> AllExceptions(HttpServletRequest request, Exception exception) {
		ResponseEntity<PastleExceptionModel> result;
		Integer code = getStatus(exception);
		code = (code == null) ? HttpStatus.INTERNAL_SERVER_ERROR.value() : code;
		PastleExceptionModel error = new PastleExceptionModel(exception.getMessage(),
				exception.getClass().getSimpleName(), request.getRequestURI(), code);
		result = new ResponseEntity<>(error, HttpStatus.valueOf(code));
		exception.printStackTrace();
		return result;
	}

	private Integer getStatus(Exception e) {
		if (e instanceof PastleyException) {
			PastleyException ex = (PastleyException) e;
			if (ex.getHttpStatus() != null) {
				return ex.getHttpStatus().value();
			}
		}
		return STATUS.get(e.getClass().getSimpleName());
	}
}
