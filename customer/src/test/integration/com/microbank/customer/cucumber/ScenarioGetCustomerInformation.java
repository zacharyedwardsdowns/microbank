package com.microbank.customer.cucumber;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import io.cucumber.java.en.When;
import org.springframework.http.HttpMethod;

public class ScenarioGetCustomerInformation extends CucumberBaseStep {
  private static final RestClient REST_CLIENT = new RestClient();

  @When("a user hits the customer information endpoint")
  public void getCustomerInformation() throws RestClientException {
    customerResponseEntity =
        REST_CLIENT.sendRequest(
            getCustomerInformationEndpoint() + customerId, HttpMethod.GET, null, Customer.class);
  }
}
