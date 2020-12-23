package com.microbank.customer.exception;

/** Thrown upon failure to register a customer */
public class FailedToRegisterCustomerException extends Exception {
  private static final long serialVersionUID = 1L;

  public FailedToRegisterCustomerException() {
    super();
  }

  public FailedToRegisterCustomerException(final String message) {
    super(message);
  }

  public FailedToRegisterCustomerException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
