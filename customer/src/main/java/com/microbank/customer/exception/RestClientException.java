package com.microbank.customer.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

/** Thrown when an http exception occurs during integration testing */
public class RestClientException extends HttpClientErrorException {

  /**
   * Throws an exception containing an HttpStatusCode.
   *
   * @param code The HttpStatusCode.
   */
  public RestClientException(final HttpStatusCode code) {
    super(code);
  }

  /**
   * Throws an exception containing an HttpStatusCode and a message.
   *
   * @param code The HttpStatusCode.
   * @param message The exception message.
   */
  public RestClientException(final HttpStatusCode code, final String message) {
    super(code, message);
  }
}
