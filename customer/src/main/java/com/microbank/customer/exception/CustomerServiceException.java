package com.microbank.customer.exception;

/**
 * Thrown when a general exception occurs.
 */
public class CustomerServiceException extends Exception {
  private static final long serialVersionUID = 1L;

  public CustomerServiceException() {
    super();
  }

  public CustomerServiceException(final String message) {
    super(message);
  }

  public CustomerServiceException(
    final String message,
    final Throwable throwable
  ) {
    super(message, throwable);
  }
}
