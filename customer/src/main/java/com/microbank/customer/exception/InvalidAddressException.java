package com.microbank.customer.exception;

/** Thrown when attempting to register with an invalid address. */
public class InvalidAddressException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidAddressException() {
    super();
  }

  public InvalidAddressException(final String message) {
    super(message);
  }

  public InvalidAddressException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
