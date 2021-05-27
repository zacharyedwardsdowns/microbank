package com.microbank.customer.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.MongoInstantiationException;
import com.microbank.customer.exception.RestClientException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Configuration
@EnableMongoRepositories(basePackages = "com.microbank.customer.repository")
public class MongoDbConfiguration extends AbstractMongoClientConfiguration {
  private static final String EXCEPTION = "Failed to obtain password from Spring Cloud!";
  private static final String ATLAS_PASSWORD = "atlas.mongodb.password";
  private static final String PROPERTY_SOURCES = "propertySources";
  private static final String SOURCE = "source";
  private final String host;
  private final String user;
  private final String pass;
  private final String db;

  @Autowired
  MongoDbConfiguration(
      @Value("${customer.mongodb.host}") final String host,
      @Value("${customer.mongodb.user}") final String user,
      @Value("${atlas.mongodb.password}") final String pass,
      @Value("${customer.mongodb.db}") final String db,
      @Nullable final Environment environment) {
    this.pass = getPassword(environment, pass);
    this.host = host;
    this.user = user;
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

  private String getPassword(@Nullable final Environment environment, final String pass) {
    if (environment != null) {
      final String cucumber = environment.getProperty("cucumber.tests");
      if (cucumber != null && cucumber.equals("true")) {
        final RestClient restClient = new RestClient();
        final String response;
        try {
          response =
              restClient
                  .sendRequest(
                      environment.getProperty("spring.cloud.config.uri"),
                      HttpMethod.GET,
                      null,
                      String.class)
                  .getBody();
        } catch (final RestClientException e) {
          throw new MongoInstantiationException(EXCEPTION, e);
        }
        if (response == null) {
          throw new MongoInstantiationException(EXCEPTION);
        } else {
          return parseSpringCloudResponse(response, pass);
        }
      }
    }
    return pass;
  }

  private String parseSpringCloudResponse(final String response, final String pass) {
    final JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
    if (jsonObject.has(PROPERTY_SOURCES) && jsonObject.get(PROPERTY_SOURCES).isJsonArray()) {
      final JsonArray jsonArray = jsonObject.get(PROPERTY_SOURCES).getAsJsonArray();
      for (final JsonElement jsonElement : jsonArray) {
        final JsonObject element = jsonElement.getAsJsonObject();
        if (element.has(SOURCE) && element.get(SOURCE).getAsJsonObject().has(ATLAS_PASSWORD)) {
          return element.get(SOURCE).getAsJsonObject().get(ATLAS_PASSWORD).getAsString();
        }
      }
    }
    return pass;
  }
}
