package com.microbank.customer.cucumber;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.Yaml;

public class CucumberBaseStep {
  private static final Logger LOG = LoggerFactory.getLogger(CucumberBaseStep.class);
  private static Map<String, Object> properties;

  protected static ResponseEntity<Customer> customerResponseEntity;
  @Autowired protected RestClient restClient;
  protected static Customer customer;
  protected static String customerId;
  protected static String accessToken;

  static {
    try {
      final URL url = new URL("file:src/test/resources/integration.yaml");
      final Yaml yaml = new Yaml();
      properties = yaml.load(url.openStream());

    } catch (final Exception e) {
      LOG.error("Failed to load integration properties due to exception: {}", e.toString());
    }
  }

  private String getBaseUri() {
    return (String) properties.get("baseUri");
  }

  protected String getRegisterEndpoint() {
    return getBaseUri() + properties.get("registerCustomer");
  }

  protected String getCustomerInformationEndpoint() {
    return getBaseUri() + properties.get("getOrDeleteCustomerInfo");
  }

  protected String readFile(final String resourcePath) throws IOException {
    final File resource = new ClassPathResource(resourcePath).getFile();
    return Sanitizer.sanitizeJson(Files.readString(resource.toPath()));
  }

  protected HttpHeaders accessToken() {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    return httpHeaders;
  }
}
