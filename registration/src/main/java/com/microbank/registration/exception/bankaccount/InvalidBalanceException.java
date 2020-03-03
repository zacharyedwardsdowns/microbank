package com.microbank.registration.exception.bankaccount;

/**
 * Exception to be thrown when attempting to register a bank account with an invalid balance.
 */
public class InvalidBalanceException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidBalanceException() {
    super();
  }

  public InvalidBalanceException(final String message) {
    super(message);
  }
}
