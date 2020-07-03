package com.microbank.customer.exception.validation;

import com.microbank.customer.exception.ValidationException;

/** Thrown when attempting to register with an invalid username. */
public class InvalidUsernameException extends ValidationException {
  private static final long serialVersionUID = 1L;

  public InvalidUsernameException() {
    super();
  }

  public InvalidUsernameException(final String message) {
    super(message);
  }

  public InvalidUsernameException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
