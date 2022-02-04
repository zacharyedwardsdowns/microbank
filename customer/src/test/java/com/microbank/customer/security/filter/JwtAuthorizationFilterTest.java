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
  private JwtAuthorizationFilter authorizationFilter;
  private static TokenGenerator mockTokenGenerator;
  private static FilterChain mockFilterChain;
  private static RSAPrivateKey privateKey;
  private static RSAPublicKey publicKey;
  private static String registrationUri;
  private static String getCustomerUri;
  private static String requestBase;
  private static String contextPath;
  private static String authPath;
  private static String authUri;

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

    contextPath = TestUtil.getYamlProperty(properties, "server.servlet.context-path");
    authPath = TestUtil.getYamlProperty(properties, "customer.request.authorize");
    requestBase = TestUtil.getYamlProperty(properties, "customer.request.base");
    registrationUri = contextPath + requestBase;
    getCustomerUri = contextPath + "/id";
    authUri = contextPath + authPath;
  }

  @BeforeEach
  void setup() {
    authorizationFilter =
        new JwtAuthorizationFilter(authPath, contextPath, requestBase, mockTokenGenerator);
  }

  @Test
  void doFilterInternalAuthUri() throws Exception {
    final ServletRequest request = new ServletRequest(authUri);
    final ServletResponse response = new ServletResponse();
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.atMostOnce()).doFilter(request, response);
  }

  @Test
  void doFilterInternalRegistrationUri() throws Exception {
    final ServletRequest request = new ServletRequest(registrationUri);
    final ServletResponse response = new ServletResponse();
    request.setMethod(HttpMethod.POST);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.atMostOnce()).doFilter(request, response);
  }

  @Test
  void doFilterInternalNullAuthorization() throws Exception {
    final ServletRequest request = new ServletRequest(getCustomerUri);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, null);
    request.setHeaders(headers);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  void doFilterInternalBadToken() throws Exception {
    final ServletRequest request = new ServletRequest(getCustomerUri);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(
        HttpHeaders.AUTHORIZATION,
        JWT.create().withSubject("").sign(Algorithm.HMAC256("signature")));
    request.setHeaders(headers);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  void doFilterInternalExpiredToken() throws Exception {
    final ServletRequest request = new ServletRequest(registrationUri);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(
        HttpHeaders.AUTHORIZATION,
        JWT.create()
            .withSubject("")
            .withExpiresAt(Date.from(Util.currentTime().minusSeconds(60)))
            .sign(Algorithm.RSA256(publicKey, privateKey)));
    request.setHeaders(headers);
    request.setMethod(HttpMethod.GET);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  void doFilterInternalSuccess() throws Exception {
    final ServletRequest request = new ServletRequest(getCustomerUri);
    final ServletResponse response = new ServletResponse();
    final HttpHeaders headers = new HttpHeaders();
    headers.set(
        HttpHeaders.AUTHORIZATION,
        JWT.create()
            .withSubject("")
            .withExpiresAt(Date.from(Util.currentTime().plusSeconds(60)))
            .sign(Algorithm.RSA256(publicKey, privateKey)));
    request.setHeaders(headers);
    authorizationFilter.doFilterInternal(request, response, mockFilterChain);
    Mockito.verify(mockFilterChain, Mockito.atMostOnce()).doFilter(request, response);
  }
}
