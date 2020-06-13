package com.microbank.customer.exception;

/**
 * Thrown when attempting to register with an invalid username.
 */
public class InvalidUsernameException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidUsernameException() {
    super();
  }

  public InvalidUsernameException(final String message) {
    super(message);
  }

  public InvalidUsernameException(
    final String message,
    final Throwable throwable
  ) {
    super(message, throwable);
  }
}
