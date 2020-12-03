package com.microbank.customer.exception;

/** Thrown when an http exception occurs during integration testing */
public class RestClientException extends Exception {
  private static final long serialVersionUID = 1L;

  public RestClientException() {
    super();
  }

  public RestClientException(final String message) {
    super(message);
  }

  public RestClientException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
