package com.microbank.customer.exception;

import jakarta.servlet.ServletException;

/** Thrown when a customer already exists. */
public class AuthorizationException extends ServletException {

  /** Throws an exception. */
  public AuthorizationException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public AuthorizationException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public AuthorizationException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
