package com.microbank.registration.exception.user;

/**
 * Exception to be thrown when attempting to register a user with an invalid dateOfBirth.
 */
public class InvalidDateOfBirthException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidDateOfBirthException() {
    super();
  }

  public InvalidDateOfBirthException(final String message) {
    super(message);
  }
}
