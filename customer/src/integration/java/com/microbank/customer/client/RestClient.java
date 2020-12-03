package com.microbank.customer.client;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import java.io.IOException;
import java.net.URI;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestClient {
  private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

  public <T> ResponseEntity<T> sendRequest(
      final String endpoint,
      final HttpMethod httpMethod,
      final String payload,
      final Class<T> clazz)
      throws RestClientException {

    final ResponseEntity<T> responseEntity;
    CloseableHttpClient httpClient = null;

    try {
      final URI uri = new URI(endpoint);
      httpClient = HttpClients.createDefault();
      final RestTemplate restTemplate =
          new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

      final HttpHeaders httpHeaders = new HttpHeaders();
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
