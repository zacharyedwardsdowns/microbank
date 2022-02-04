package com.microbank.customer.exception;

/** Thrown upon failure to initialize a class */
public class InitializationException extends RuntimeException {

  /** Throws an exception. */
  public InitializationException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public InitializationException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public InitializationException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
