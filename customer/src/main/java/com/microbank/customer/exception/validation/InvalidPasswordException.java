package com.microbank.customer.exception.validation;

import com.microbank.customer.exception.ValidationException;

/** Thrown when attempting to register with an invalid password. */
public class InvalidPasswordException extends ValidationException {
  private static final long serialVersionUID = 1L;

  public InvalidPasswordException() {
    super();
  }

  public InvalidPasswordException(final String message) {
    super(message);
  }

  public InvalidPasswordException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
