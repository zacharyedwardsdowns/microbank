package com.microbank.customer.client;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClientTest {
  private static final HttpMethod HTTP_METHOD = HttpMethod.GET;
  private static final Class<String> CLAZZ = String.class;
  private static final String ENDPOINT = "test.com";
  @Mock private RestTemplate mockRestTemplate;
  private RestClient restClient;
  private String json;

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    restClient = new RestClient();

    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @Test
  public void sendRequest() throws Exception {
    final ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.OK);

    Mockito.when(
            mockRestTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.eq(HTTP_METHOD),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(CLAZZ)))
        .thenReturn(response);

    Assert.assertEquals(
        response, restClient.sendRequest(ENDPOINT, HTTP_METHOD, json, CLAZZ, mockRestTemplate));
  }

  @Test(expected = RestClientException.class)
  public void sendRequestRestClientException() throws Exception {
    Mockito.when(
            mockRestTemplate.exchange(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.eq(HTTP_METHOD),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.eq(CLAZZ)))
        .thenThrow(org.springframework.web.client.RestClientException.class);

    restClient.sendRequest(ENDPOINT, HTTP_METHOD, json, CLAZZ, mockRestTemplate);
  }
}
