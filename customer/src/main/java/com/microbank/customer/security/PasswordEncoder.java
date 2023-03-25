package com.microbank.customer.security;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

/** Contains methods for hashing and matching passwords */
public final class PasswordEncoder {
  private static final Pbkdf2PasswordEncoder pbkdf2Encoder =
      Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

  /** Prevents instantiation. */
  private PasswordEncoder() {}

  /**
   * Generates a hashed password using the Pbkdf2PasswordEncoder default settings.
   *
   * @param password The password to hash.
   * @return A hashed password.
   */
  public static String generateHash(final String password) {
    return pbkdf2Encoder.encode(password);
  }

  /**
   * Checks if the given password matches the given hash.
   *
   * @param password Password to test.
   * @param hash Hash to match against.
   * @return True if the password and hash match.
   */
  public static boolean matchesHash(final String password, final String hash) {
    return pbkdf2Encoder.matches(password, hash);
  }
}
