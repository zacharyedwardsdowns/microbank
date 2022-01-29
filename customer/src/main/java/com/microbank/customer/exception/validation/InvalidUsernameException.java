package com.microbank.customer.exception.validation;

import com.microbank.customer.exception.ValidationException;
import java.io.Serial;

/** Thrown when attempting to register with an invalid username. */
public class InvalidUsernameException extends ValidationException {
  @Serial private static final long serialVersionUID = 1L;

  /** Throws an exception. */
  public InvalidUsernameException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public InvalidUsernameException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public InvalidUsernameException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
