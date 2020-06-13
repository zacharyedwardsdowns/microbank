package com.microbank.customer.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.FileCopyUtils;

@Configuration
@EnableMongoRepositories(basePackages = "com.microbank.customer.repository")
public class MongoDbConfiguration extends AbstractMongoClientConfiguration {
  private static final Logger LOG = LoggerFactory.getLogger(
    MongoDbConfiguration.class
  );

  private final String host;
  private final String user;
  private final String db;

  MongoDbConfiguration(
    @Value("${customer.mongodb.host}") String host,
    @Value("${customer.mongodb.user}") String user,
    @Value("${customer.mongodb.db}") String db
  ) {
    this.host = host;
    this.user = user;
    this.db = db;
  }

  @Override
  public MongoClient mongoClient() {
    final String pass = getPassword();

    final StringBuilder connectionString = new StringBuilder()
      .append("mongodb+srv://")
      .append(user)
      .append(":")
      .append(pass)
      .append("@")
      .append(host)
      .append("/")
      .append(db)
      .append("?retryWrites=true&w=majority");

    return MongoClients.create(connectionString.toString());
  }

  @Override
  protected String getDatabaseName() {
    return db;
  }

  private String getPassword() {
    try {
      return FileCopyUtils.copyToString(
        new InputStreamReader(
          new ClassPathResource("mongodb-password").getInputStream()
        )
      );
    } catch (final Exception e) {
      LOG.error("Failed to obtain the MongoDB Atlas password!", e);
      return "";
    }
  }
}
