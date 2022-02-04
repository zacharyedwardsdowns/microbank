package com.microbank.customer.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.microbank.customer.exception.InitializationException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.model.Tokens;
import com.microbank.customer.util.Util;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Generates ID, access, and refresh tokens. */
@Component
public class TokenGenerator {
  private static final Base64.Decoder decoder = Base64.getDecoder();
  private static final KeyFactory FACTORY;
  private final RSAPrivateKey refreshPrivate;
  private final RSAPublicKey refreshPublic;
  private final RSAPrivateKey accessPrivate;
  private final RSAPublicKey accessPublic;
  private final RSAPrivateKey idPrivate;
  private final RSAPublicKey idPublic;
  private final long refreshExpiration;
  private final long accessExpiration;
  private final long idExpiration;
  private final String audience;
  private final String issuer;

  static {
    try {
      FACTORY = KeyFactory.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      throw new InitializationException("Failed to obtain RSA KeyFactory!", e);
    }
  }

  /**
   * Injects the necessary dependencies.
   *
   * @param refreshExpiration Seconds until refresh token expires.
   * @param refreshPrivate Base64 encoded RSA private key for refresh token.
   * @param refreshPublic Base64 encoded RSA public key for refresh token.
   * @param accessExpiration Seconds until access token expires.
   * @param accessPrivate Base64 encoded RSA private key for access token.
   * @param accessPublic Base64 encoded RSA public key for access token.
   * @param idExpiration Seconds until id token expires.
   * @param idPrivate Base64 encoded RSA private key for id token.
   * @param idPublic Base64 encoded RSA public key for id token.
   * @param audience The intended audience of all 3 tokens.
   * @param issuer The issuer of all 3 tokens.
   * @throws InvalidKeySpecException Thrown upon failure of base64ToPrivateKey().
   */
  @Autowired
  public TokenGenerator(
      @Value("${token.customer.key.refresh.expire}") final long refreshExpiration,
      @Value("${token.customer.key.refresh.private}") final String refreshPrivate,
      @Value("${token.customer.key.refresh.public}") final String refreshPublic,
      @Value("${token.customer.key.access.expire}") final long accessExpiration,
      @Value("${token.customer.key.access.private}") final String accessPrivate,
      @Value("${token.customer.key.access.public}") final String accessPublic,
      @Value("${token.customer.key.id.expire}") final long idExpiration,
      @Value("${token.customer.key.id.private}") final String idPrivate,
      @Value("${token.customer.key.id.public}") final String idPublic,
      @Value("${token.customer.audience}") final String audience,
      @Value("${token.customer.issuer}") final String issuer)
      throws InvalidKeySpecException {
    this.refreshPrivate = base64ToPrivateKey(refreshPrivate);
    this.refreshPublic = base64ToPublicKey(refreshPublic);
    this.accessPrivate = base64ToPrivateKey(accessPrivate);
    this.accessPublic = base64ToPublicKey(accessPublic);
    this.idPrivate = base64ToPrivateKey(idPrivate);
    this.idPublic = base64ToPublicKey(idPublic);
    this.refreshExpiration = refreshExpiration;
    this.accessExpiration = accessExpiration;
    this.idExpiration = idExpiration;
    this.audience = audience;
    this.issuer = issuer;
  }

  /**
   * Generates an ID, access, and refresh token intended to be used by the frontend.
   *
   * @param customer Used to add customer information to the tokens.
   * @return A Tokens object containing all 3 tokens.
   */
  public Tokens generateTokens(final Customer customer) {
    final Instant instant = Util.currentTime();
    final Date now = Date.from(instant);
    final Date idExp = Date.from(instant.plusSeconds(idExpiration));
    final Date accessExp = Date.from(instant.plusSeconds(accessExpiration));
    final Date refreshExp = Date.from(instant.plusSeconds(refreshExpiration));

    final String idToken =
        JWT.create()
            .withIssuedAt(now)
            .withNotBefore(now)
            .withIssuer(issuer)
            .withExpiresAt(idExp)
            .withAudience(audience)
            .withClaim(
                "name",
                Util.getName(
                    customer.getFirstName(),
                    customer.getMiddleName(),
                    customer.getLastName(),
                    customer.getSuffix()))
            .withSubject(customer.getUsername())
            .withClaim("suffix", customer.getSuffix())
            .withClaim("lastName", customer.getLastName())
            .withClaim("firstName", customer.getFirstName())
            .withClaim("middleName", customer.getMiddleName())
            .sign(Algorithm.RSA256(idPublic, idPrivate));

    final String accessToken =
        JWT.create()
            .withIssuedAt(now)
            .withNotBefore(now)
            .withIssuer(issuer)
            .withAudience(audience)
            .withExpiresAt(accessExp)
            .withSubject(customer.getUsername())
            .sign(Algorithm.RSA256(accessPublic, accessPrivate));

    final String refreshToken =
        JWT.create()
            .withIssuedAt(now)
            .withNotBefore(now)
            .withIssuer(issuer)
            .withAudience(audience)
            .withExpiresAt(refreshExp)
            .withSubject(customer.getUsername())
            .sign(Algorithm.RSA256(refreshPublic, refreshPrivate));

    return new Tokens(idToken, accessToken, refreshToken);
  }

  /**
   * Convert a Base64 encoded string into an RSAPublicKey.
   *
   * @param key A Base64 encoded string.
   * @return An RSAPublicKey.
   * @throws InvalidKeySpecException Failure to generate PublicKey.
   */
  public static RSAPublicKey base64ToPublicKey(final String key) throws InvalidKeySpecException {
    return (RSAPublicKey) FACTORY.generatePublic(new X509EncodedKeySpec(decoder.decode(key)));
  }

  /**
   * Convert a Base64 encoded string into an RSAPrivateKey.
   *
   * @param key A Base64 encoded string.
   * @return An RSAPrivateKey.
   * @throws InvalidKeySpecException Failure to generate PrivateKey.
   */
  public static RSAPrivateKey base64ToPrivateKey(final String key) throws InvalidKeySpecException {
    return (RSAPrivateKey) FACTORY.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(key)));
  }

  /** @return RSAPublicKey for verifying the access token signature */
  public RSAPublicKey getAccessPublic() {
    return accessPublic;
  }
}
