package com.microbank.customer.cucumber;

import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import io.cucumber.java.en.When;
import java.net.URISyntaxException;
import org.springframework.http.HttpMethod;

public class ScenarioDeleteCustomerInformation extends CucumberBaseStep {

  @When("a user hits the delete customer information endpoint")
  public void deleteCustomerInformation() throws RestClientException, URISyntaxException {
    customerResponseEntity =
        restClient.sendRequest(
            deleteCustomerUri(), HttpMethod.DELETE, null, Customer.class, accessToken());
  }
}
