package com.microbank.customer.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.microbank.customer.model.ServletRequest;
import com.microbank.customer.model.ServletResponse;
import com.microbank.customer.security.TokenGenerator;
import com.microbank.customer.util.TestUtil;
import com.microbank.customer.util.Util;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import javax.servlet.FilterChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.yaml.snakeyaml.nodes.MappingNode;

class JwtAuthorizationFilterTest {
  private static final String CONTEXT_PATH = "/microbank-customer";
  private static final String REQUEST_BASE = "/customer";
  private static final String AUTH_PATH = "/authorize";
  private JwtAuthorizationFilter authorizationFilter;
  private static TokenGenerator mockTokenGenerator;
  private static final String REGISTRATION_URI;
  private static FilterChain mockFilterChain;
  private static RSAPrivateKey privateKey;
  private static RSAPublicKey publicKey;
  private static final String AUTH_URI;

  static {
    AUTH_URI = CONTEXT_PATH + REQUEST_BASE + AUTH_PATH;
    REGISTRATION_URI = CONTEXT_PATH + REQUEST_BASE;
  }

  @BeforeAll
  static void setupClass() throws Exception {
    mockFilterChain = Mockito.mock(FilterChain.class);
    mockTokenGenerator = Mockito.mock(TokenGenerator.class);
    final MappingNode properties = TestUtil.getYamlProperties("application.yaml");
    privateKey =
        TokenGenerator.base64ToPrivateKey(
            TestUtil.getYamlProperty(properties, "token.customer.key.access.private"));
    publicKey =
        TokenGenerator.base64ToPublicKey(
            TestUtil.getYamlProperty(properties, "token.customer.key.access.public"));
    Mockito.when(mockTokenGenerator.getAccessPublic()).thenReturn(publicKey);
  }

  @BeforeEach
  void setup() {
    authorizationFilter =
        new JwtAuthorizationFilter(AUTH_PATH, CONTEXT_PATH, REQUEST_BASE, mockTokenGenerator);
  }

  @Test
  void doFilterInternalAuthUri() throws Exception {
    final ServletRequest request = new ServletRequest(AUTH_URI);
    final ServletResponse response = new ServletResponse();
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.atMostOnce()).doFilter(request, response);
  }

  @Test
  void doFilterInternalRegistrationUri() throws Exception {
    final ServletRequest request = new ServletRequest(REGISTRATION_URI);
    final ServletResponse response = new ServletResponse();
    request.setMethod(HttpMethod.POST);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.atMostOnce()).doFilter(request, response);
  }

  @Test
  void doFilterInternalNullAuthorization() throws Exception {
    final ServletRequest request = new ServletRequest(CONTEXT_PATH);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, null);
    request.setHeaders(headers);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  void doFilterInternalBadToken() throws Exception {
    final ServletRequest request = new ServletRequest(CONTEXT_PATH);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(
        HttpHeaders.AUTHORIZATION,
        JWT.create().withSubject(CONTEXT_PATH).sign(Algorithm.HMAC256(CONTEXT_PATH)));
    request.setHeaders(headers);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  void doFilterInternalExpiredToken() throws Exception {
    final ServletRequest request = new ServletRequest(REGISTRATION_URI);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(
        HttpHeaders.AUTHORIZATION,
        JWT.create()
            .withSubject(CONTEXT_PATH)
            .withExpiresAt(Date.from(Util.currentTime().minusSeconds(60)))
            .sign(Algorithm.RSA256(publicKey, privateKey)));
    request.setHeaders(headers);
    request.setMethod(HttpMethod.GET);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  void doFilterInternalSuccess() throws Exception {
    final ServletRequest request = new ServletRequest(CONTEXT_PATH);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(
        HttpHeaders.AUTHORIZATION,
        JWT.create()
            .withSubject(CONTEXT_PATH)
            .withExpiresAt(Date.from(Util.currentTime().plusSeconds(60)))
            .sign(Algorithm.RSA256(publicKey, privateKey)));
    request.setHeaders(headers);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.atMostOnce()).doFilter(request, response);
  }
}
