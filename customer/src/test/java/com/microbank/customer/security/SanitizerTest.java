package com.microbank.customer.security;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class SanitizerTest {

  private static String json;

  @BeforeAll
  static void setupClass() throws Exception {
    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    // Replace newlines with a space after them and then just newlines with an empty string.
    json = json.replace("\r\n ", "").replace("\n ", "").replace("\r\n", "").replace("\n", "");
  }

  @Test
  void testSanitizeJson() {
    final String result = Sanitizer.sanitizeJson(json);
    Assertions.assertEquals(json, result);
  }
}
