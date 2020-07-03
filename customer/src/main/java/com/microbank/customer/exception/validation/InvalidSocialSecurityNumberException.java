package com.microbank.customer.exception.validation;

import com.microbank.customer.exception.ValidationException;

/** Thrown when attempting to register with an invalid socialSecurityNumber. */
public class InvalidSocialSecurityNumberException extends ValidationException {
  private static final long serialVersionUID = 1L;

  public InvalidSocialSecurityNumberException() {
    super();
  }

  public InvalidSocialSecurityNumberException(final String message) {
    super(message);
  }

  public InvalidSocialSecurityNumberException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
