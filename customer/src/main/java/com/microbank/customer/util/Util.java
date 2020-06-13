package com.microbank.customer.util;

import org.springframework.data.domain.ExampleMatcher;

/**
 * A general utility class.
 */
public final class Util {

  /**
   * Prevents instantiation.
   */
  private Util() {}

  public static ExampleMatcher defaultMatcher() {
    return ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase();
  }
}
