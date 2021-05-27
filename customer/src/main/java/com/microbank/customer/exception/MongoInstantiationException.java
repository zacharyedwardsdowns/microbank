package com.microbank.customer.exception;

/** Thrown upon failure to instantiate MongoDbConfiguration. */
public class MongoInstantiationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MongoInstantiationException() {
    super();
  }

  public MongoInstantiationException(final String message) {
    super(message);
  }

  public MongoInstantiationException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
