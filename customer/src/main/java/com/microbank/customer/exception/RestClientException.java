package com.microbank.customer.exception;

/** Thrown when an http exception occurs during integration testing */
public class RestClientException extends Exception {

  /** Throws an exception. */
  public RestClientException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public RestClientException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public RestClientException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
