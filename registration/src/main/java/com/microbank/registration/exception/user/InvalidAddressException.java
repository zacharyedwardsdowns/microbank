package com.microbank.registration.exception.user;

/**
 * Exception to be thrown when attempting to register a user with an invalid address.
 */
public class InvalidAddressException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidAddressException() {
    super();
  }

  public InvalidAddressException(final String message) {
    super(message);
  }
}
