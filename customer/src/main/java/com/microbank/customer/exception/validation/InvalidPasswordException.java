package com.microbank.customer.exception.validation;

import com.microbank.customer.exception.ValidationException;

/** Thrown when attempting to register with an invalid password. */
public class InvalidPasswordException extends ValidationException {

  /** Throws an exception. */
  public InvalidPasswordException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public InvalidPasswordException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public InvalidPasswordException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
