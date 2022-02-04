package com.microbank.customer.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.microbank.customer.security.TokenGenerator;
import com.microbank.customer.util.Util;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Filter to check for access token. */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
  private final String authorizationUri;
  private final String registrationUri;
  private final Algorithm algorithm;

  /**
   * Injects the necessary dependencies.
   *
   * @param authorizationPath The request path for the authorization endpoint.
   * @param contextPath The context path of the application.
   * @param requestBase The request path of the CustomerController.
   * @param tokenGenerator Contains the access key public token.
   */
  public JwtAuthorizationFilter(
      @Value("${customer.request.authorize}") final String authorizationPath,
      @Value("${server.servlet.context-path}") final String contextPath,
      @Value("${customer.request.base}") final String requestBase,
      final TokenGenerator tokenGenerator) {
    algorithm = Algorithm.RSA256(tokenGenerator.getAccessPublic(), null);
    authorizationUri = contextPath + requestBase + authorizationPath;
    registrationUri = contextPath + requestBase;
  }

  /**
   * Filter that prevents access to the service if authorization.
   *
   * @param request Gets request URI.
   * @param response Sets status if authorization fails.
   * @param filterChain Sends request to service if authorization is successful.
   * @throws IOException Thrown by filterChan.doFilter(request, response).
   * @throws ServletException Thrown by filterChan.doFilter(request, response).
   */
  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws IOException, ServletException {
    if (request.getRequestURI().equals(authorizationUri)
        || (request.getRequestURI().equals(registrationUri)
            && HttpMethod.POST.matches(request.getMethod()))) {
      filterChain.doFilter(request, response);
      return;
    }

    final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (Util.nullOrEmpty(authorization)) {
      LOG.debug("Must provide authorization to access this service!");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    final String token = authorization.replace("Bearer ", "");
    final DecodedJWT jwt;

    try {
      jwt = JWT.decode(token);
      algorithm.verify(jwt);
    } catch (final JWTVerificationException e) {
      LOG.debug("Failed to verify the authorization token!");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    if (jwt.getExpiresAt().before(Date.from(Util.currentTime()))) {
      LOG.debug("The authorization token has expired!");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    filterChain.doFilter(request, response);
  }
}
