package com.microbank.customer.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.json.JsonSanitizer;
import com.microbank.customer.exception.InvalidJsonException;
import com.microbank.customer.util.Util;
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

  /**
   * Sanitizes json using Jsoup and JsonSanitizer then maps it to an object using Jackson.
   *
   * @param <T> Object of type clazz.
   * @param json The json string to sanitize and map.
   * @param clazz The class of the object to map too.
   * @return An object created from the given json.
   * @throws InvalidJsonException Failed to map the json to an object of the given class.
   */
  public static <T> T sanitizeAndMap(final String json, final Class<T> clazz)
      throws InvalidJsonException {
    final String sanitizeJson = sanitizeJson(json);
    try {
      return Util.MAPPER.readValue(sanitizeJson, clazz);
    } catch (final JsonProcessingException e) {
      throw new InvalidJsonException(
          "Failed to create an instance of" + clazz.getCanonicalName() + "with the given json!", e);
    }
  }
}
