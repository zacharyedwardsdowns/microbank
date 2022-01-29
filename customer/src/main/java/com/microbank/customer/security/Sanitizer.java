package com.microbank.customer.security;

import com.google.json.JsonSanitizer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/** Contains sanitization methods for user input. */
public final class Sanitizer {

  /** Prevents instantiation. */
  private Sanitizer() {}

  /**
   * Sanitizes a given string using Jsoup.
   *
   * @param string The string to sanitize.
   * @return A sanitized string.
   */
  public static String sanitizeString(final String string) {
    return Jsoup.clean(string, Safelist.basic());
  }

  /**
   * Sanitizes json using Jsoup and JsonSanitizer.
   *
   * @param json The json string to sanitize.
   * @return A sanitized json.
   */
  public static String sanitizeJson(final String json) {
    return JsonSanitizer.sanitize(sanitizeString(json));
  }
}
