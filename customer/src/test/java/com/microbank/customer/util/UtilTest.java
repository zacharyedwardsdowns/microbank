package com.microbank.customer.util;

import com.microbank.customer.security.model.Tokens;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class UtilTest {

  static Stream<String> nullOrEmptyStrings() {
    return java.util.stream.Stream.of(null, "", "null");
  }

  @ParameterizedTest
  @MethodSource("nullOrEmptyStrings")
  void nullOrEmpty(final String string) {
    Assertions.assertTrue(Util.nullOrEmpty(string));
  }

  static Stream<Tokens> tokensNotNullStream() {
    return java.util.stream.Stream.of(
        null, new Tokens(null, null, null), new Tokens("", null, null), new Tokens("", "", null));
  }

  @ParameterizedTest
  @MethodSource("tokensNotNullStream")
  void tokensNotNull(final Tokens tokens) {
    Assertions.assertFalse(Util.tokensNotNull(tokens));
  }

  @Test
  void concatNull() {
    Assertions.assertEquals("", Util.concatNull(null));
    Assertions.assertEquals("s", Util.concatNull("s"));
  }

  @Test
  void getName() {
    Assertions.assertEquals("f s", Util.getName("f", null, null, "s"));
    Assertions.assertEquals("f m l", Util.getName("f", "m", "l", null));
    Assertions.assertEquals("", Util.getName(null, null, null, null));
  }
}
