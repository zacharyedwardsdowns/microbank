package com.microbank.customer.util;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.MongoInstantiationException;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import com.mongodb.client.MongoClient;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MongoDbConfigurationTest extends MongoDbConfiguration {

  private static final String HOST = "freecluster-s3rjf.mongodb.net";
  private static final String DB = "test-microbank";
  private static final String USER = "tester";
  private static final String PASS = "test";

  private MongoDbConfiguration mongoDbConfiguration;
  @Mock private Environment mockEnvironment;
  @Mock private RestClient mockRestClient;
  private String json;

  MongoDbConfigurationTest() {
    super(HOST, USER, PASS, DB, null, null);
  }

  @BeforeEach
  void setup() throws Exception {
    MockitoAnnotations.openMocks(this);
    mongoDbConfiguration =
        new MongoDbConfiguration(HOST, USER, PASS, DB, mockEnvironment, mockRestClient);

    final File resource = new ClassPathResource("json/SpringCloudAtlasConfig.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @Test
  void testMongoClient() {
    final MongoClient mongoClient = mongoDbConfiguration.mongoClient();
    Assertions.assertNotNull(mongoClient);
  }

  @Test
  void testGetDatabaseName() {
    Assertions.assertEquals(DB, mongoDbConfiguration.getDatabaseName());
  }

  @Test
  void testGetPassword() throws Exception {

    Mockito.when(this.mockEnvironment.getProperty("cucumber.tests")).thenReturn("true");
    Mockito.when(
            this.mockRestClient.sendRequest(
                ArgumentMatchers.isNull(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq(String.class)))
        .thenReturn(new ResponseEntity<>(json, HttpStatus.OK));

    Assertions.assertEquals(
        "test", mongoDbConfiguration.getPassword(mockEnvironment, "bad", mockRestClient));
  }

  @Test
  void testGetPasswordNullResponse() throws Exception {

    Mockito.when(this.mockEnvironment.getProperty("cucumber.tests")).thenReturn("true");
    Mockito.when(
            this.mockRestClient.sendRequest(
                ArgumentMatchers.isNull(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq(String.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    Assertions.assertThrows(
        MongoInstantiationException.class,
        () -> mongoDbConfiguration.getPassword(mockEnvironment, "bad", mockRestClient));
  }

  @Test
  void testGetPasswordRestClientException() throws Exception {

    Mockito.when(this.mockEnvironment.getProperty("cucumber.tests")).thenReturn("true");
    Mockito.when(
            this.mockRestClient.sendRequest(
                ArgumentMatchers.isNull(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq(String.class)))
        .thenThrow(RestClientException.class);

    Assertions.assertThrows(
        MongoInstantiationException.class,
        () -> mongoDbConfiguration.getPassword(mockEnvironment, "bad", mockRestClient));
  }
}
