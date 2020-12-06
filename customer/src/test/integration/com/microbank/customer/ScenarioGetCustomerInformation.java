package com.microbank.customer;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.io.IOException;
import org.junit.Assert;
import org.springframework.http.HttpMethod;

public class ScenarioGetCustomerInformation extends CucumberBaseStep {

  private static final RestClient REST_CLIENT = new RestClient();

  @Given("a username for a registered user")
  public void getRegisteredUsername() throws IOException {
    final String customerJson = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerJson, Customer.class);
    Assert.assertNotNull(customer);
    Assert.assertNotNull(customer.getUsername());
  }

  @When("a user hits the customer information endpoint")
  public void getCustomerInformation() throws RestClientException {
    customerResponseEntity =
        REST_CLIENT.sendRequest(
            getCustomerInformationEndpoint() + customer.getUsername(),
            HttpMethod.GET,
            null,
            Customer.class);
  }
}
