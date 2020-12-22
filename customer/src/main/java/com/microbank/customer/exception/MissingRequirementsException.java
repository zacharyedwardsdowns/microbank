package com.microbank.customer.exception;

/** Thrown when not given enough information to fulfill the request. */
public class MissingRequirementsException extends Exception {
  private static final long serialVersionUID = 1L;

  public MissingRequirementsException() {
    super();
  }

  public MissingRequirementsException(final String message) {
    super(message);
  }

  public MissingRequirementsException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
