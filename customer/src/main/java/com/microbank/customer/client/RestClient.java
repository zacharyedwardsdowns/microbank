package com.microbank.customer.client;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** Component used to send http request to other services. */
@Component
public class RestClient {
  private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

  /**
   * Sends and http request to the given endpoint and returns a ResponseEntity of type clazz..
   *
   * @param endpoint The endpoint to send an http request to.
   * @param httpMethod The method of the request.
   * @param payload The body (if any) of the request.
   * @param clazz The class of the response.
   * @param <T> Allows the response type to be generic.
   * @return A generic Mono.
   * @throws RestClientException If a request exception occurs.
   * @throws URISyntaxException If the given endpoint is syntactically invalid.
   */
  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz)
      throws RestClientException, URISyntaxException {
    return sendRequest(endpoint, httpMethod, payload, clazz, null, null);
  }

  /**
   * Sends and http request to the given endpoint and returns a ResponseEntity of type clazz..
   *
   * @param endpoint The endpoint to send an http request to.
   * @param httpMethod The method of the request.
   * @param payload The body (if any) of the request.
   * @param clazz The class of the response.
   * @param headers Http request headers to send.
   * @param <T> Allows the response type to be generic.
   * @return A generic Mono.
   * @throws RestClientException If a request exception occurs.
   * @throws URISyntaxException If the given endpoint is syntactically invalid.
   */
  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz,
      final MultiValueMap<String, String> headers)
      throws RestClientException, URISyntaxException {
    return sendRequest(endpoint, httpMethod, payload, clazz, headers, null);
  }

  /**
   * Sends and http request to the given endpoint and returns a ResponseEntity of type clazz.
   *
   * @param endpoint The endpoint to send an http request to.
   * @param httpMethod The method of the request.
   * @param payload The body (if any) of the request.
   * @param clazz The class of the response.
   * @param headers Http request headers to send.
   * @param webClient Allows for mocking the WebClient.
   * @param <T> Allows the response type to be generic.
   * @return A generic Mono.
   * @throws RestClientException If a request exception occurs.
   * @throws URISyntaxException If the given endpoint is syntactically invalid.
   */
  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz,
      MultiValueMap<String, String> headers,
      WebClient webClient)
      throws RestClientException, URISyntaxException {

    final URI uri = new URI(endpoint);

    if (webClient == null) {
      webClient = WebClient.create();
    }

    if (headers == null) headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    final MultiValueMap<String, String> finalHeaders = headers;
    final Consumer<HttpHeaders> httpHeaders = map -> map.addAll(finalHeaders);

    String sanitizedPayload = null;
    if (payload != null) {
      sanitizedPayload = Sanitizer.sanitizeJson(payload);
    }

    final WebClient.ResponseSpec response;
    if (sanitizedPayload == null) {
      response = webClient.method(httpMethod).uri(uri).headers(httpHeaders).retrieve();
    } else {
      response =
          webClient
              .method(httpMethod)
              .uri(uri)
              .headers(httpHeaders)
              .bodyValue(sanitizedPayload)
              .retrieve();
    }

    return response
        .onStatus(
            HttpStatusCode::isError,
            errorResponse -> {
              LOG.warn("WebClient request failed with error response: {}", errorResponse);
              return Mono.error(new RestClientException(errorResponse.statusCode()));
            })
        .toEntity(clazz)
        .block();
  }
}
