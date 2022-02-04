package com.microbank.customer.security;

import com.microbank.customer.model.Customer;
import com.microbank.customer.security.model.Tokens;
import com.microbank.customer.util.TestUtil;
import com.microbank.customer.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.nodes.MappingNode;

class TokenGeneratorTest {
  private static final long expiration = 100;
  private TokenGenerator tokenGenerator;
  private static String privateKey;
  private static String publicKey;

  @BeforeAll
  static void setupClass() throws Exception {
    final MappingNode properties = TestUtil.getYamlProperties("application.yaml");
    privateKey = TestUtil.getYamlProperty(properties, "token.customer.key.access.private");
    publicKey = TestUtil.getYamlProperty(properties, "token.customer.key.access.public");
  }

  @BeforeEach
  void setup() throws Exception {
    tokenGenerator =
        new TokenGenerator(
            expiration,
            privateKey,
            publicKey,
            expiration,
            privateKey,
            publicKey,
            expiration,
            privateKey,
            publicKey,
            "",
            "");
  }

  @Test
  void generateTokens() {
    final Tokens tokens = tokenGenerator.generateTokens(new Customer());
    Assertions.assertTrue(Util.tokensNotNull(tokens));
  }

  @Test
  void getAccessPublic() throws Exception {
    Assertions.assertEquals(
        TokenGenerator.base64ToPublicKey(publicKey), tokenGenerator.getAccessPublic());
  }
}
