package com.microbank.customer.exception;

/** Thrown upon failure to transform a user given json into a class */
public class InvalidJsonException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidJsonException() {
    super();
  }

  public InvalidJsonException(final String message) {
    super(message);
  }

  public InvalidJsonException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
