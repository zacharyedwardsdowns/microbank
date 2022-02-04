package com.microbank.customer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microbank.customer.security.model.Tokens;
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
   * Checks if the given string is null, empty, or "null".
   *
   * @param string The string to null/empty check.
   * @return True if the given string is null or empty and false otherwise.
   */
  public static boolean nullOrEmpty(final String string) {
    return string == null || string.equals("") || string.equalsIgnoreCase("null");
  }

  /**
   * Checks if all tokens are set.
   *
   * @param tokens The tokens to check.
   * @return True if all tokens are present and false if any are missing.
   */
  public static boolean tokensNotNull(final Tokens tokens) {
    return !(tokens == null
        || tokens.getIdToken() == null
        || tokens.getAccessToken() == null
        || tokens.getRefreshToken() == null);
  }

  /**
   * Set null strings to empty strings.
   *
   * @param string String to check if null.
   * @return Empty string if null, unchanged otherwise.
   */
  public static String concatNull(String string) {
    if (string == null) {
      return "";
    }
    return string;
  }

  /**
   * Concatenate first, middle, last, and suffix names into a full name.
   *
   * @param firstName User's first name.
   * @param middleName User's middle name.
   * @param lastName User's last name.
   * @param suffix User's suffix.
   * @return A full name.
   */
  public static String getName(
      final String firstName, final String middleName, final String lastName, final String suffix) {
    String name = concatNull(firstName);
    if (!nullOrEmpty(name)) {
      if (!nullOrEmpty(middleName)) {
        name += " " + middleName;
      }
    } else {
      name = concatNull(middleName);
    }
    if (!nullOrEmpty(name)) {
      if (!nullOrEmpty(lastName)) {
        name += " " + lastName;
      }
    } else {
      name = concatNull(lastName);
    }
    if (!nullOrEmpty(name) && !nullOrEmpty(suffix)) {
      name += " " + suffix;
    }
    return name;
  }
}
