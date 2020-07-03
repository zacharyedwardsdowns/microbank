package com.microbank.customer.exception;

/** Thrown as the super class of validation exceptions */
public class ValidationException extends Exception {
  private static final long serialVersionUID = 1L;

  public ValidationException() {
    super();
  }

  public ValidationException(final String message) {
    super(message);
  }

  public ValidationException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
