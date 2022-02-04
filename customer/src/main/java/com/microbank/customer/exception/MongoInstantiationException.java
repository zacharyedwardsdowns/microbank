package com.microbank.customer.exception;

/** Thrown upon failure to instantiate MongoDbConfiguration. */
public class MongoInstantiationException extends RuntimeException {

  /** Throws an exception. */
  public MongoInstantiationException() {
    super();
  }

  /**
   * Throws an exception containing a message.
   *
   * @param message The exception message.
   */
  public MongoInstantiationException(final String message) {
    super(message);
  }

  /**
   * Throws an exception containing a message and the throwable that caused it.
   *
   * @param message The exception message.
   * @param throwable The cause of the exception.
   */
  public MongoInstantiationException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
