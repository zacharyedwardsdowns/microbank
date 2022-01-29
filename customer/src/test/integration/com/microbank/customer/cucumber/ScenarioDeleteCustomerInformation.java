package com.microbank.customer.cucumber;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import io.cucumber.java.en.When;
import org.springframework.http.HttpMethod;

public class ScenarioDeleteCustomerInformation extends CucumberBaseStep {
  private static final RestClient REST_CLIENT = new RestClient();

  @When("a user hits the delete customer information endpoint")
  public void deleteCustomerInformation() throws RestClientException {
    customerResponseEntity =
        REST_CLIENT.sendRequest(
            getCustomerInformationEndpoint() + customerId, HttpMethod.DELETE, null, Customer.class);
  }
}
