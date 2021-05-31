package com.microbank.customer.util;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.MongoInstantiationException;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.security.Sanitizer;
import com.mongodb.client.MongoClient;
import java.io.File;
import java.nio.file.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MongoDbConfigurationTest extends MongoDbConfiguration {

  private static final String HOST = "freecluster-s3rjf.mongodb.net";
  private static final String DB = "test-microbank";
  private static final String USER = "tester";
  private static final String PASS = "test";

  private MongoDbConfiguration mongoDbConfiguration;
  @Mock private Environment mockEnvironment;
  @Mock private RestClient mockRestClient;
  private String json;

  public MongoDbConfigurationTest() {
    super(HOST, USER, PASS, DB, null);
  }

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    mongoDbConfiguration = new MongoDbConfiguration(HOST, USER, PASS, DB, mockEnvironment);

    final File resource = new ClassPathResource("json/SpringCloudAtlasConfig.json").getFile();
    json = Files.readString(resource.toPath());
    json = Sanitizer.sanitizeJson(json);
  }

  @Test
  public void testMongoClient() {
    final MongoClient mongoClient = mongoDbConfiguration.mongoClient();
    Assert.assertNotNull(mongoClient);
  }

  @Test
  public void testGetDatabaseName() {
    Assert.assertEquals(DB, mongoDbConfiguration.getDatabaseName());
  }

  @Test
  public void testGetPassword() throws Exception {

    Mockito.when(this.mockEnvironment.getProperty("cucumber.tests")).thenReturn("true");
    Mockito.when(
            this.mockRestClient.sendRequest(
                ArgumentMatchers.isNull(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq(String.class)))
        .thenReturn(new ResponseEntity<>(json, HttpStatus.OK));

    Assert.assertEquals(
        "test", mongoDbConfiguration.getPassword(mockEnvironment, "bad", mockRestClient));
  }

  @Test(expected = MongoInstantiationException.class)
  public void testGetPasswordNullResponse() throws Exception {

    Mockito.when(this.mockEnvironment.getProperty("cucumber.tests")).thenReturn("true");
    Mockito.when(
            this.mockRestClient.sendRequest(
                ArgumentMatchers.isNull(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq(String.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    mongoDbConfiguration.getPassword(mockEnvironment, "bad", mockRestClient);
  }

  @Test(expected = MongoInstantiationException.class)
  public void testGetPasswordRestClientException() throws Exception {

    Mockito.when(this.mockEnvironment.getProperty("cucumber.tests")).thenReturn("true");
    Mockito.when(
            this.mockRestClient.sendRequest(
                ArgumentMatchers.isNull(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq(String.class)))
        .thenThrow(RestClientException.class);

    mongoDbConfiguration.getPassword(mockEnvironment, "bad", mockRestClient);
  }
}
