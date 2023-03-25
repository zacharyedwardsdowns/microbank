package com.microbank.customer.cucumber;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.model.Tokens;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpMethod;

public class ScenarioAuthorization extends CucumberBaseStep {
  private String customerPayload;
  private Tokens tokens;

  @Given("a username and password")
  public void getRegistrationInformation() throws IOException {
    customerPayload = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerPayload, Customer.class);
    Assertions.assertNotNull(customer);
    Assertions.assertNotNull(customer.getUsername());
    Assertions.assertNotNull(customer.getPassword());
  }

  @When("a user calls the authorize endpoint")
  public void registerACustomer() throws RestClientException, URISyntaxException {
    tokens =
        restClient
            .sendRequest(authorizationUri(), HttpMethod.POST, customerPayload, Tokens.class)
            .getBody();
  }

  @Then("they receive an access token in response")
  public void authorize() {
    Assertions.assertNotNull(tokens);
    Assertions.assertNotNull(tokens.getAccessToken());
    accessToken = tokens.getAccessToken();
  }
}
