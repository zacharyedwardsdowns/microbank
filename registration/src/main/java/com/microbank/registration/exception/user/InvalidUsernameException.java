package com.microbank.registration.exception.user;

/**
 * Exception to be thrown when attempting to register a user with an invalid username.
 */
public class InvalidUsernameException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidUsernameException() {
    super();
  }

  public InvalidUsernameException(final String message) {
    super(message);
  }
}
