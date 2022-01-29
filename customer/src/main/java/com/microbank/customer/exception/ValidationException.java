package com.microbank.customer.exception;

import java.io.Serial;

/** Thrown as the super class of validation exceptions */
public class ValidationException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  /** Throws an exception. */
  public ValidationException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public ValidationException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public ValidationException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
