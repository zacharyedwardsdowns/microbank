package com.microbank.customer.exception;

import java.io.Serial;

/** Thrown when a customer already exists. */
public class ExistingCustomerException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  /** Throws an exception. */
  public ExistingCustomerException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public ExistingCustomerException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public ExistingCustomerException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
