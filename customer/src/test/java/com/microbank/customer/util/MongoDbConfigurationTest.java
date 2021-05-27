package com.microbank.customer.util;

import com.mongodb.client.MongoClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MongoDbConfigurationTest extends MongoDbConfiguration {

  private static final String HOST = "freecluster-s3rjf.mongodb.net";
  private static final String DB = "test-microbank";
  private static final String USER = "tester";
  private static final String PASS = "test";
  private MongoDbConfiguration mongoDbConfiguration;

  public MongoDbConfigurationTest() {
    super(HOST, USER, PASS, DB);
  }

  @Before
  public void setup() {
    mongoDbConfiguration = new MongoDbConfiguration(HOST, USER, PASS, DB);
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
}
