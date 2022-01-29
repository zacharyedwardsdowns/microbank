package com.microbank.customer.exception;

import java.io.Serial;

/** Thrown when not given enough information to fulfill the request. */
public class MissingRequirementsException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  /** Throws an exception. */
  public MissingRequirementsException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public MissingRequirementsException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public MissingRequirementsException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
