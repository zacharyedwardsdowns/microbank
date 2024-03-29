package com.microbank.customer.cucumber;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class ScenarioCustomerRegistration extends CucumberBaseStep {
  private String customerPayload;

  @Given("a user's registration information")
  public void getRegistrationInformation() throws IOException {
    customerPayload = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerPayload, Customer.class);
    Assertions.assertNotNull(customer);
  }

  @When("they hit the customer registration endpoint")
  public void registerACustomer() throws RestClientException, URISyntaxException {
    customerResponseEntity =
        restClient.sendRequest(customerUri(), HttpMethod.POST, customerPayload, Customer.class);
  }

  @And("a status code of 201 is received")
  public void statusCode201() {
    Assertions.assertNotNull(customerResponseEntity);
    Assertions.assertEquals(HttpStatus.CREATED, customerResponseEntity.getStatusCode());
    customerResponseEntity = null;
  }
}
