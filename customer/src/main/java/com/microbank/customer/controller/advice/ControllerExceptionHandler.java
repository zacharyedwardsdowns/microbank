package com.microbank.customer.controller.advice;

import com.microbank.customer.exception.*;
import com.microbank.customer.exception.model.ExceptionCause;
import com.microbank.customer.exception.model.ExceptionResponse;
import com.microbank.customer.util.Util;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = ExistingCustomerException.class)
  protected ResponseEntity<ExceptionResponse> conflictException(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.CONFLICT);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  @ExceptionHandler(value = ResourceNotFoundException.class)
  protected ResponseEntity<ExceptionResponse> notFoundException(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  @ExceptionHandler(
      value = {
        ValidationException.class,
        InvalidJsonException.class,
        MissingRequirementsException.class
      })
  protected ResponseEntity<ExceptionResponse> badRequestException(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  @ExceptionHandler(
      value = {
        IOException.class,
        RuntimeException.class,
        NullPointerException.class,
        DataAccessException.class,
        FailedToRegisterCustomerException.class
      })
  protected ResponseEntity<ExceptionResponse> internalServerErrorException(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  @NonNull
  @Override
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleExceptionInternal(
      @NonNull final Exception e,
      @Nullable final Object body,
      @Nullable final HttpHeaders headers,
      @NonNull final HttpStatus status,
      @NonNull final WebRequest request) {
    return new ResponseEntity<>(createDefaultExceptionResponse(e, request, status), status);
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
      final Exception e, final HttpServletRequest request, final HttpStatus status) {
    final ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setPath(request.getRequestURI());
    return createDefaultExceptionResponseHelper(e, exceptionResponse, status);
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
      final Exception e, final WebRequest request, final HttpStatus status) {
    final ExceptionResponse exceptionResponse = new ExceptionResponse();
    exceptionResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
    return createDefaultExceptionResponseHelper(e, exceptionResponse, status);
  }

  /**
   * A helper method that does most of the exception response building.
   *
   * @param e The exception to build a message with.
   * @param exceptionResponse An exception response with the URI.
   * @param status The status to respond with.
   * @return A default ExceptionResponse.
   */
  private ExceptionResponse createDefaultExceptionResponseHelper(
      final Exception e, final ExceptionResponse exceptionResponse, final HttpStatus status) {
    exceptionResponse.setTimestamp(Util.currentTime());
    exceptionResponse.setStatus(status.value());
    exceptionResponse.setError(e.getClass().getSimpleName());
    exceptionResponse.setMessage(e.getMessage());
    exceptionResponse.setCause(fillCause(e));
    exceptionResponse.setTrace(ExceptionUtils.getStackTrace(e));
    return exceptionResponse;
  }

  /**
   * Using a given Exception fill an ExceptionCause recursively till all causes are contained.
   *
   * @param e The base Exception.
   * @return An ExceptionCause containing all causes.
   */
  private ExceptionCause fillCause(final Throwable e) {
    final ExceptionCause exceptionCause = new ExceptionCause();
    final Throwable cause = e.getCause();
    if (cause != null) {
      exceptionCause.setError(cause.getClass().getSimpleName());
      exceptionCause.setMessage(cause.getMessage());
      exceptionCause.setCause(fillCause(cause));
      return exceptionCause;
    } else {
      return null;
    }
  }
}
