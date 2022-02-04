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
  protected static ResponseEntity<Customer> customerResponseEntity;
  @Autowired protected RestClient restClient;
  protected static String accessToken;
  protected static String customerId;
  protected static Customer customer;

  private static final String getOrDelete;
  private static final String register;
  private static final String baseUri;

  static {
    try {
      final MappingNode properties = TestUtil.getYamlProperties("application.yaml");
      baseUri = TestUtil.getYamlProperty(properties, "integration.base.uri");
      register = TestUtil.getYamlProperty(properties, "integration.registerCustomer.uri");
      getOrDelete = TestUtil.getYamlProperty(properties, "integration.getOrDeleteCustomer.uri");
    } catch (IOException e) {
      throw new InitializationException("Failed to retrieve yaml properties!", e);
    }
  }

  protected String getRegisterEndpoint() {
    return baseUri + register;
  }

  protected String getCustomerInformationEndpoint() {
    return baseUri + getOrDelete;
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
