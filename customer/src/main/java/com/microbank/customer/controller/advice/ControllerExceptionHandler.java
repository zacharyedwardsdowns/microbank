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

/** Advice to create an ExceptionResponse for the controller when an exception is thrown. */
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles the response for exceptions returning HttpStatus.CONFLICT.
   *
   * @param e The exception being handled.
   * @param request The http request containing the uri.
   * @return An ExceptionResponse with useful information.
   */
  @ExceptionHandler(value = ExistingCustomerException.class)
  protected ResponseEntity<ExceptionResponse> conflict(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.CONFLICT);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  /**
   * Handles the response for exceptions returning HttpStatus.NOT_FOUND.
   *
   * @param e The exception being handled.
   * @param request The http request containing the uri.
   * @return An ExceptionResponse with useful information.
   */
  @ExceptionHandler(value = ResourceNotFoundException.class)
  protected ResponseEntity<ExceptionResponse> notFound(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  /**
   * Handles the response for exceptions returning HttpStatus.BAD_REQUEST.
   *
   * @param e The exception being handled.
   * @param request The http request containing the uri.
   * @return An ExceptionResponse with useful information.
   */
  @ExceptionHandler(
      value = {
        ValidationException.class,
        InvalidJsonException.class,
        MissingRequirementsException.class
      })
  protected ResponseEntity<ExceptionResponse> badRequest(
      final Exception e, final HttpServletRequest request) {
    final ExceptionResponse exceptionResponse =
        createDefaultExceptionResponse(e, request, HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(
        exceptionResponse, HttpStatus.valueOf(exceptionResponse.getStatus()));
  }

  /**
   * Handles the response for exceptions returning HttpStatus.INTERNAL_SERVER_ERROR.
   *
   * @param e The exception being handled.
   * @param request The http request containing the uri.
   * @return An ExceptionResponse with useful information.
   */
  @ExceptionHandler(
      value = {
        IOException.class,
        RuntimeException.class,
        NullPointerException.class,
        DataAccessException.class,
        FailedToRegisterCustomerException.class
      })
  protected ResponseEntity<ExceptionResponse> internalServerError(
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
