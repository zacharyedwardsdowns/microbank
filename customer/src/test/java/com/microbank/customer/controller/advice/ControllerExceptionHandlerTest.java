package com.microbank.customer.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microbank.customer.exception.ExistingCustomerException;
import com.microbank.customer.exception.FailedToRegisterCustomerException;
import com.microbank.customer.exception.ResourceNotFoundException;
import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.exception.model.ExceptionResponse;
import com.microbank.customer.model.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class ControllerExceptionHandlerTest {
  private static final ControllerExceptionHandler HANDLER = new ControllerExceptionHandler();
  private static final HttpServletRequest servletRequest;
  private static final String MESSAGE = "message";
  private static final WebRequest webRequest;
  private static final String URI = "URI";

  static {
    servletRequest = new ServletRequest(URI);
    webRequest = new ServletWebRequest(servletRequest);
  }

  @Test
  void conflict() {
    final ExceptionResponse exceptionResponse =
        HANDLER.conflict(new ExistingCustomerException(MESSAGE), servletRequest).getBody();
    assertExceptionResponse(
        exceptionResponse, HttpStatus.CONFLICT, ExistingCustomerException.class);
  }

  @Test
  void notFound() {
    final ExceptionResponse exceptionResponse =
        HANDLER.notFound(new ResourceNotFoundException(MESSAGE), servletRequest).getBody();
    assertExceptionResponse(
        exceptionResponse, HttpStatus.NOT_FOUND, ResourceNotFoundException.class);
  }

  @Test
  void badRequest() {
    final ExceptionResponse exceptionResponse =
        HANDLER.badRequest(new ValidationException(MESSAGE), servletRequest).getBody();
    assertExceptionResponse(exceptionResponse, HttpStatus.BAD_REQUEST, ValidationException.class);
  }

  @Test
  void httpClientErrorException() {
    final ExceptionResponse exceptionResponse =
        HANDLER
            .httpClientErrorException(
                new HttpClientErrorException(HttpStatus.BAD_REQUEST), servletRequest)
            .getBody();
    assertExceptionResponse(
        exceptionResponse,
        HttpStatus.BAD_REQUEST,
        HttpClientErrorException.class,
        HttpStatus.BAD_REQUEST.toString(),
        false);
  }

  @Test
  void internalServerError() {
    final ExceptionResponse exceptionResponse =
        HANDLER
            .internalServerError(
                new FailedToRegisterCustomerException(MESSAGE, new IOException(MESSAGE)),
                servletRequest)
            .getBody();
    assertExceptionResponse(
        exceptionResponse,
        HttpStatus.INTERNAL_SERVER_ERROR,
        FailedToRegisterCustomerException.class,
        MESSAGE,
        true);
  }

  @Test
  void handleExceptionInternal() {
    final Object response =
        HANDLER
            .handleExceptionInternal(
                new Exception(MESSAGE), null, null, HttpStatus.INTERNAL_SERVER_ERROR, webRequest)
            .getBody();
    Assertions.assertNotNull(response);
    final ExceptionResponse exceptionResponse =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .convertValue(response, ExceptionResponse.class);
    assertExceptionResponse(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR, Exception.class);
  }

  private <T> void assertExceptionResponse(
      final ExceptionResponse exceptionResponse, final HttpStatus status, final Class<T> clazz) {
    assertExceptionResponse(exceptionResponse, status, clazz, MESSAGE, false);
  }

  private <T> void assertExceptionResponse(
      final ExceptionResponse exceptionResponse,
      final HttpStatus status,
      final Class<T> clazz,
      final String message,
      final boolean cause) {
    Assertions.assertNotNull(exceptionResponse);
    Assertions.assertNotNull(exceptionResponse.getTrace());
    Assertions.assertNotNull(exceptionResponse.getTimestamp());
    Assertions.assertEquals(URI, exceptionResponse.getPath());
    Assertions.assertEquals(message, exceptionResponse.getMessage());
    Assertions.assertEquals(status.value(), exceptionResponse.getStatus());
    Assertions.assertEquals(clazz.getSimpleName(), exceptionResponse.getError());

    if (cause) {
      Assertions.assertNotNull(exceptionResponse.getCause());
    } else {
      Assertions.assertNull(exceptionResponse.getCause());
    }
  }
}
