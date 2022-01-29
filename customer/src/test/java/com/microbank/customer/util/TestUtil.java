package com.microbank.customer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;

/** A testing utility class. */
public final class TestUtil {

  /** Prevents instantiation. */
  private TestUtil() {}

  /**
   * Verifies the given response contains an error with the name of the given class.
   *
   * @param response The response containing the given class name.
   * @param clazz The class to check for in the response.
   * @throws JsonProcessingException Failure to map the response to JsonNode.
   */
  public static void verifyError(final String response, final Class<?> clazz)
      throws JsonProcessingException {
    Assertions.assertNotNull(response);
    final JsonNode result = Util.MAPPER.readTree(response);
    Assertions.assertEquals(result.get("error").asText(), clazz.getSimpleName());
  }
}
