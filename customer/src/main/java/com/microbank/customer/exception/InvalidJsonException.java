package com.microbank.customer.exception;

/** Thrown upon failure to transform a user given json into a class */
public class InvalidJsonException extends Exception {

  /** Throws an exception. */
  public InvalidJsonException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public InvalidJsonException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public InvalidJsonException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
