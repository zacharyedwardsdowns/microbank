package com.microbank.customer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import org.springframework.data.domain.ExampleMatcher;

/** A general utility class. */
public final class Util {

  /** Prevents instantiation. */
  private Util() {}

  /** A general ObjectMapper for Jackson operations. */
  public static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

  /**
   * Provides a default ExampleMatcher for database queries.
   *
   * @return The default ExampleMatcher.
   */
  public static ExampleMatcher defaultMatcher() {
    return ExampleMatcher.matching().withIgnoreNullValues();
  }

  /**
   * Returns current time in UTC as an Instant.
   *
   * @return The current time.
   */
  public static Instant currentTime() {
    return Instant.now();
  }

  /**
   * Returns current time added with the given seconds in UTC as an Instant.
   *
   * @param seconds Amount of seconds to add to the current time.
   * @return The current time plus the given seconds.
   */
  public static Instant currentTimePlusSeconds(final long seconds) {
    return Instant.now().plusSeconds(seconds);
  }

  /**
   * Checks if the given string is null, empty, or "null".
   *
   * @param string The string to null/empty check.
   * @return True if the given string is null or empty and false otherwise.
   */
  public static boolean nullOrEmpty(final String string) {
    return string == null || string.equals("") || string.equalsIgnoreCase("null");
  }
}
