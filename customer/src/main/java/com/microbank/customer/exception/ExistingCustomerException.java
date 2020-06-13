package com.microbank.customer.exception;

/**
 * Thrown when a general exception occurs.
 */
public class ExistingCustomerException extends Exception {
  private static final long serialVersionUID = 1L;

  public ExistingCustomerException() {
    super();
  }

  public ExistingCustomerException(final String message) {
    super(message);
  }

  public ExistingCustomerException(
    final String message,
    final Throwable throwable
  ) {
    super(message, throwable);
  }
}
