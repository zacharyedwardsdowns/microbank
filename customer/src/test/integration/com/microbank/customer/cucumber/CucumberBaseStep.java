package com.microbank.customer.cucumber;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import com.microbank.customer.util.TestUtil;
import com.thoughtworks.xstream.InitializationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.nodes.MappingNode;

public class CucumberBaseStep {
  private static final String SLASH = "/";
  private static final String AUTH_URI;
  private static final String BASE_URI;

  protected static ResponseEntity<Customer> customerResponseEntity;
  @Autowired protected RestClient restClient;
  protected static String accessToken;
  protected static String customerId;
  protected static Customer customer;

  static {
    try {
      final MappingNode properties = TestUtil.getYamlProperties("application.yml");
      BASE_URI = TestUtil.getYamlProperty(properties, "integration.request.base");
      AUTH_URI = TestUtil.getYamlProperty(properties, "customer.request.authorize");
    } catch (IOException e) {
      throw new InitializationException("Failed to retrieve yaml properties!", e);
    }
  }

  protected String customerUri() {
    return BASE_URI;
  }

  protected String authorizationUri() {
    return BASE_URI + AUTH_URI;
  }

  protected String customerInfoUri() {
    return BASE_URI + SLASH + customerId;
  }

  protected String deleteCustomerUri() {
    return BASE_URI + SLASH + customerId;
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
