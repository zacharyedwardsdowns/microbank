package com.microbank.customer;

import com.microbank.customer.model.Customer;
import com.microbank.customer.security.Sanitizer;
import io.cucumber.spring.CucumberContextConfiguration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.Yaml;

@CucumberContextConfiguration
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberBaseStep {
  private static final Logger LOG = LoggerFactory.getLogger(CucumberBaseStep.class);
  private static Map<String, Object> properties;

  public static ResponseEntity<Customer> customerResponseEntity;
  public static Customer customer;

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
    return (String) properties.get("base");
  }

  public String getRegisterEndpoint() {
    return getBaseUri() + properties.get("register");
  }

  public String getCustomerInformationEndpoint() {
    return getBaseUri() + properties.get("getCustomerInformation");
  }

  public String readFile(final String resourcePath) throws IOException {
    final File resource = new ClassPathResource(resourcePath).getFile();
    return Sanitizer.sanitizeJson(Files.readString(resource.toPath()));
  }
}
