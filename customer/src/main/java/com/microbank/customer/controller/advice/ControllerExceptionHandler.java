package com.microbank.customer.controller.advice;

import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.InvalidJsonException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.exception.model.ExceptionCause;
import com.microbank.customer.exception.model.ExceptionResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(value = ExistingCustomerException.class)
  protected ResponseEntity<ExceptionResponse> existingCustomerException(
      Exception e, HttpServletRequest request) {
    ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.CONFLICT);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  @ExceptionHandler(value = {InvalidJsonException.class, ValidationException.class})
  protected ResponseEntity<ExceptionResponse> invalidJsonException(
      Exception e, HttpServletRequest request) {
    ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  /**
   * Creates a default ExceptionResponse from the given exception and request. Uses the given status
   * as the response status of the controller.
   *
   * @param e The exception to build a message with.
   * @param request The request to get the URI with.
   * @param status The status to respond with.
   * @return A default ExceptionResponse.
   */
  private ExceptionResponse createDefaultExceptionResponse(
      Exception e, HttpServletRequest request, HttpStatus status) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setTimestamp(now());
    exceptionResponse.setStatus(status.value());
    exceptionResponse.setError(e.getClass().getSimpleName());
    exceptionResponse.setMessage(e.getMessage());
    exceptionResponse.setPath(request.getRequestURI());
    exceptionResponse.setCause(fillCause(e));
    return exceptionResponse;
  }

  /**
   * Using a given Exception fill an ExceptionCause recursively till all causes are contained.
   *
   * @param e The base Exception.
   * @return An ExceptionCause containing all causes.
   */
  private ExceptionCause fillCause(Throwable e) {
    ExceptionCause exceptionCause = new ExceptionCause();
    Throwable cause = e.getCause();
    if (cause != null) {
      exceptionCause.setError(cause.getClass().getSimpleName());
      exceptionCause.setMessage(cause.getMessage());
      exceptionCause.setCause(fillCause(cause));
      return exceptionCause;
    } else {
      return null;
    }
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
