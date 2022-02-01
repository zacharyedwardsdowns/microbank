package com.microbank.customer.client;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import java.io.IOException;
import java.net.URI;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
   * @return A generic ResponseEntity.
   * @throws RestClientException If a request exception occurs.
   */
  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz)
      throws RestClientException {
    return sendRequest(endpoint, httpMethod, payload, clazz, null, null);
  }

  /**
   * Sends and http request to the given endpoint and returns a ResponseEntity of type clazz..
   *
   * @param endpoint The endpoint to send an http request to.
   * @param httpMethod The method of the request.
   * @param payload The body (if any) of the request.
   * @param clazz The class of the response.
   * @param httpHeaders Http request headers to send.
   * @param <T> Allows the response type to be generic.
   * @return A generic ResponseEntity.
   * @throws RestClientException If a request exception occurs.
   */
  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz,
      final HttpHeaders httpHeaders)
      throws RestClientException {
    return sendRequest(endpoint, httpMethod, payload, clazz, httpHeaders, null);
  }

  /**
   * Sends and http request to the given endpoint and returns a ResponseEntity of type clazz.
   *
   * @param endpoint The endpoint to send an http request to.
   * @param httpMethod The method of the request.
   * @param payload The body (if any) of the request.
   * @param clazz The class of the response.
   * @param httpHeaders Http request headers to send.
   * @param restTemplate Allows for mocking the RestTemplate.
   * @param <T> Allows the response type to be generic.
   * @return A generic ResponseEntity.
   * @throws RestClientException If a request exception occurs.
   */
  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz,
      HttpHeaders httpHeaders,
      RestTemplate restTemplate)
      throws RestClientException {

    final ResponseEntity<T> responseEntity;
    CloseableHttpClient httpClient = null;

    try {
      final URI uri = new URI(endpoint);
      httpClient = HttpClients.createDefault();

      if (restTemplate == null) {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
      }

      if (httpHeaders == null) httpHeaders = new HttpHeaders();
      httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
      httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

      String sanitizedPayload = null;
      if (payload != null) {
        sanitizedPayload = Sanitizer.sanitizeJson(payload);
      }

      final HttpEntity<String> httpEntity = new HttpEntity<>(sanitizedPayload, httpHeaders);
      responseEntity = restTemplate.exchange(uri, httpMethod, httpEntity, clazz);

    } catch (final Exception e) {
      throw new RestClientException("RestClient failed due to exception: ", e);
    } finally {
      if (httpClient != null) {
        try {
          httpClient.close();
        } catch (final IOException e) {
          LOG.error("Failed to close the CloseableHttpClient!", e);
        }
      }
    }

    return responseEntity;
  }
}
