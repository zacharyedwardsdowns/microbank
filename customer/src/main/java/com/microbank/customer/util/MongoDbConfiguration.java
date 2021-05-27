package com.microbank.customer.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

@Configuration
@EnableMongoRepositories(basePackages = "com.microbank.customer.repository")
public class MongoDbConfiguration extends AbstractMongoClientConfiguration {
  private final String host;
  private final String user;
  private final String pass;
  private final String db;

  MongoDbConfiguration(
      @Value("${customer.mongodb.host}") final String host,
      @Value("${customer.mongodb.user}") final String user,
      @Value("${atlas.mongodb.password}") final String pass,
      @Value("${customer.mongodb.db}") final String db) {
    this.host = host;
    this.user = user;
    this.pass = pass;
    this.db = db;
  }

  @NonNull
  @Override
  public MongoClient mongoClient() {
    final String connectionString =
        "mongodb+srv://"
            + user
            + ":"
            + pass
            + "@"
            + host
            + "/"
            + db
            + "?retryWrites=true&w=majority";

    return MongoClients.create(connectionString);
  }

  @NonNull
  @Override
  protected String getDatabaseName() {
    return db;
  }
}
