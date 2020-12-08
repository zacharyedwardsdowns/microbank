package com.microbank.customer.feature;

import com.microbank.customer.CucumberBaseStep;
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
            getCustomerInformationEndpoint() + customer.getUsername(),
            HttpMethod.DELETE,
            null,
            Customer.class);
  }
}
