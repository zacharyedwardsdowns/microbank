package com.microbank.customer.exception.handler;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.model.exception.ExceptionResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = ExistingCustomerException.class)
  protected ResponseEntity<ExceptionResponse> existingCustomerException(
      Exception e, HttpServletRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setTimestamp(now());
    exceptionResponse.setStatus(HttpStatus.CONFLICT.value());
    exceptionResponse.setError("ExistingCustomerException");
    exceptionResponse.setMessage(e.getMessage());
    exceptionResponse.setPath(request.getRequestURI());
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  /**
   * Method to retrieve the current date and time as a string.
   *
   * @return The date and time of when the method was called.
   */
  private String now() {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
  }
}
