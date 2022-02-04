package com.microbank.customer.cucumber;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import io.cucumber.java.en.When;
import org.springframework.http.HttpMethod;

public class ScenarioGetCustomerInformation extends CucumberBaseStep {

  @When("a user hits the customer information endpoint")
  public void getCustomerInformation() throws RestClientException {
    customerResponseEntity =
        restClient.sendRequest(
            customerUri() + customerId, HttpMethod.GET, null, Customer.class, accessToken());
  }
}
