package com.microbank.customer.client;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

class RestClientTest {
  private static final ResponseEntity<String> RESPONSE_ENTITY =
      new ResponseEntity<>("", HttpStatus.OK);
  private static final ClientResponse CLIENT_RESPONSE =
      ClientResponse.create(HttpStatus.OK).body("").build();
  private static final HttpMethod HTTP_METHOD = HttpMethod.GET;
  private static final Class<String> CLAZZ = String.class;
  private static final String ENDPOINT = "test.com";
  private RestClient restClient;
  private AutoCloseable mocks;
  private WebClient webClient;
  private String json;

  @Mock private ExchangeFunction exchangeFunctionMock;

  @BeforeEach
  void setup() throws Exception {
    mocks = MockitoAnnotations.openMocks(this);
    webClient = WebClient.builder().exchangeFunction(exchangeFunctionMock).build();
    restClient = new RestClient();

    final File resource = new ClassPathResource("json/Customer.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @AfterEach
  void destroy() throws Exception {
    mocks.close();
  }

  @Test
  void sendRequest() throws Exception {
    Mockito.when(exchangeFunctionMock.exchange(ArgumentMatchers.any(ClientRequest.class)))
        .thenReturn(Mono.just(CLIENT_RESPONSE));

    Assertions.assertEquals(
        RESPONSE_ENTITY,
        restClient.sendRequest(ENDPOINT, HTTP_METHOD, json, CLAZZ, new HttpHeaders(), webClient));
  }

  @Test()
  void sendRequestRestClientException() {
    Mockito.when(exchangeFunctionMock.exchange(ArgumentMatchers.any(ClientRequest.class)))
        .thenReturn(
            Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).body("").build()));

    Assertions.assertThrows(
        RestClientException.class,
        () -> restClient.sendRequest(ENDPOINT, HTTP_METHOD, null, CLAZZ, null, webClient));
  }
}
