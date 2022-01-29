package com.microbank.customer.exception;

import java.io.Serial;

/** Thrown upon failure to register a customer */
public class FailedToRegisterCustomerException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  /** Throws an exception. */
  public FailedToRegisterCustomerException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public FailedToRegisterCustomerException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public FailedToRegisterCustomerException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
