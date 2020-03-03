package com.microbank.registration.exception;

/**
 * Exception to be thrown when attempting to register with an invalid date.
 */
public class InvalidDateException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidDateException() {
    super();
  }

  public InvalidDateException(final String message) {
    super(message);
  }
}
