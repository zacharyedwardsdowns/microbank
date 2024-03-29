package com.microbank.customer.exception;

/** Thrown when a customer is not found. */
public class ResourceNotFoundException extends Exception {

  /** Throws an exception. */
  public ResourceNotFoundException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public ResourceNotFoundException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public ResourceNotFoundException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
