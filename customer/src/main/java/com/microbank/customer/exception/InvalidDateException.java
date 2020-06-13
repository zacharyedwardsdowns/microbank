package com.microbank.customer.exception;

/**
 * Thrown when attempting to register with an invalid date.
 */
public class InvalidDateException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidDateException() {
    super();
  }

  public InvalidDateException(final String message) {
    super(message);
  }

  public InvalidDateException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
