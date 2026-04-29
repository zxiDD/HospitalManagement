package com.cg.exception;

import com.cg.dto.ErrorMessageDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 🔴 1. Resource Not Found (404)
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessageDto handleNotFound(ResourceNotFoundException ex) {

		ErrorMessageDto dto = new ErrorMessageDto();
		dto.setErrMsg(ex.getMessage());
		dto.setTimeStamp(LocalDateTime.now());
		dto.setStatus(HttpStatus.NOT_FOUND.toString());

		return dto;
	}

	// 🔴 2. Duplicate Resource (409)
	@ExceptionHandler(DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessageDto handleDuplicate(DuplicateResourceException ex) {

		ErrorMessageDto dto = new ErrorMessageDto();
		dto.setErrMsg(ex.getMessage());
		dto.setTimeStamp(LocalDateTime.now());
		dto.setStatus(HttpStatus.CONFLICT.toString());

		return dto;
	}

	// 🔴 3. Bad Request (400)
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessageDto handleBadRequest(BadRequestException ex) {

		ErrorMessageDto dto = new ErrorMessageDto();
		dto.setErrMsg(ex.getMessage());
		dto.setTimeStamp(LocalDateTime.now());
		dto.setStatus(HttpStatus.BAD_REQUEST.toString());

		return dto;
	}

	// 🔴 4. Validation Errors (IMPORTANT)
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessageDto handleValidation(ValidationException ex) {

		Map<String, List<String>> errMap = new HashMap<>();

		for (FieldError fr : ex.getErrors()) {

			if (errMap.containsKey(fr.getField())) {
				errMap.get(fr.getField()).add(fr.getDefaultMessage());
			} else {
				List<String> list = new ArrayList<>();
				list.add(fr.getDefaultMessage());
				errMap.put(fr.getField(), list);
			}
		}

		ErrorMessageDto dto = new ErrorMessageDto();
		dto.setErrMsg("Validation failed");
		dto.setErrMap(errMap);
		dto.setTimeStamp(LocalDateTime.now());
		dto.setStatus(HttpStatus.BAD_REQUEST.toString());

		return dto;
	}

	// 🔴 5. JSON / Date format error
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessageDto handleParsingError(HttpMessageNotReadableException ex) {

		String msg = "Invalid request format";

		if (ex.getMessage().contains("LocalDateTime")) {
			msg = "Enter date in proper format (yyyy-MM-dd'T'HH:mm:ss)";
		}

		ErrorMessageDto dto = new ErrorMessageDto();
		dto.setErrMsg(msg);
		dto.setTimeStamp(LocalDateTime.now());
		dto.setStatus(HttpStatus.BAD_REQUEST.toString());

		return dto;
	}

	// 🔴 6. Generic Exception (500)
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessageDto handleGeneric(Exception ex) {

		ErrorMessageDto dto = new ErrorMessageDto();
		dto.setErrMsg(ex.getMessage());
		dto.setTimeStamp(LocalDateTime.now());
		dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());

		return dto;
	}
}