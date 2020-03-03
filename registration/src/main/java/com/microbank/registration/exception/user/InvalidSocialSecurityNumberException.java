package com.microbank.registration.exception.user;

/**
 * Exception to be thrown when attempting to register a user with an invalid socialSecurityNumber.
 */
public class InvalidSocialSecurityNumberException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidSocialSecurityNumberException() {
    super();
  }

  public InvalidSocialSecurityNumberException(final String message) {
    super(message);
  }
}
