package com.microbank.customer.exception;

/** Thrown when a customer is not found */
public class ResourceNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException() {
    super();
  }

  public ResourceNotFoundException(final String message) {
    super(message);
  }

  public ResourceNotFoundException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
