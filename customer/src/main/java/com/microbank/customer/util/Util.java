package com.microbank.customer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.ExampleMatcher;

/** A general utility class. */
public final class Util {

  /** Prevents instantiation. */
  private Util() {}

  public static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

  /**
   * Provides a default ExampleMatcher for database queries.
   *
   * @return The default ExampleMatcher.
   */
  public static ExampleMatcher defaultMatcher() {
    return ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase();
  }
}
