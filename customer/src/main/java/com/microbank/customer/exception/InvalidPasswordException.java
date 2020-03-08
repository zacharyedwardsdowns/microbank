package com.microbank.customer.exception;

/**
 * Thrown when attempting to register with an invalid password.
 */
public class InvalidPasswordException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidPasswordException() {
    super();
  }

  public InvalidPasswordException(final String message) {
    super(message);
  }
}
