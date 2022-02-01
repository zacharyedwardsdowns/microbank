package com.microbank.customer.cucumber;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.security.model.Token;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpMethod;

public class ScenarioAuthorization extends CucumberBaseStep {
  private String customerPayload;
  private Token token;

  @Given("a username and password")
  public void getRegistrationInformation() throws IOException {
    customerPayload = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerPayload, Customer.class);
    Assertions.assertNotNull(customer);
    Assertions.assertNotNull(customer.getUsername());
    Assertions.assertNotNull(customer.getPassword());
  }

  @When("a user calls the authorize endpoint")
  public void registerACustomer() throws RestClientException {
    token =
        restClient
            .sendRequest(
                "http://localhost:6010/microbank-customer/customer/authorize",
                HttpMethod.POST,
                customerPayload,
                Token.class)
            .getBody();
  }

  @Then("they receive an access token in response")
  public void authorize() {
    Assertions.assertNotNull(token);
    Assertions.assertNotNull(token.getAccessToken());
    accessToken = token.getAccessToken();
  }
}
