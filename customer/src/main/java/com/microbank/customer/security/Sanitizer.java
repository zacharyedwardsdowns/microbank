package com.microbank.customer.security;

import com.google.json.JsonSanitizer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Contains sanitization methods for user input. */
public final class Sanitizer {

  /**
   * Sanitizes json using Jsoup and JsonSanitizer.
   *
   * @param json The json string to sanitize.
   * @return A sanitized json.
   */
  public static String sanitizeJson(String json) {
    return JsonSanitizer.sanitize(Jsoup.clean(json, Whitelist.basic()));
  }
}
