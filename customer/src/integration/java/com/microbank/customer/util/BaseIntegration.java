package com.microbank.customer.util;

import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.Yaml;

public class BaseIntegration {
  private static final Logger LOG = LoggerFactory.getLogger(BaseIntegration.class);
  private static Map<String, Object> properties;

  private static ResponseEntity<Customer> customerResponseEntity;

  static {
    try {
      final URL url = new URL("file:src/integration/resources/application.yaml");
      final Yaml yaml = new Yaml();
      properties = yaml.load(url.openStream());

    } catch (final Exception e) {
      LOG.error("Failed to load integration properties due to exception: {}", e.toString());
    }
  }

  private String getBaseUri() {
    return (String) properties.get("base");
  }

  public String getRegisterEndpoint() {
    return getBaseUri() + properties.get("register");
  }

  public String readFile(final String resourcePath) throws IOException {
    final File resource = new ClassPathResource(resourcePath).getFile();
    return Sanitizer.sanitizeJson(Files.readString(resource.toPath()));
  }

  public static ResponseEntity<Customer> getCustomerResponseEntity() {
    return customerResponseEntity;
  }

  public static void setCustomerResponseEntity(
      final ResponseEntity<Customer> customerResponseEntity) {
    BaseIntegration.customerResponseEntity = customerResponseEntity;
  }
}
