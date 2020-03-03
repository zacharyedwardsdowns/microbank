package com.microbank.registration.exception.user;

/**
 * Exception to be thrown when attempting to register a user with an invalid password.
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
