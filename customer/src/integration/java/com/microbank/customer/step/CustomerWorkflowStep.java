package com.microbank.customer.step;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.util.BaseIntegration;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.springframework.http.HttpMethod;

public class CustomerWorkflowStep extends BaseIntegration {

  private static final RestClient REST_CLIENT = new RestClient();
  private String customerPayload;
  private Customer customer;

  @Given("a user's registration information")
  public void getRegistrationInformation() throws IOException {
    customerPayload = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerPayload, Customer.class);
    Assert.assertNotNull(customer);
  }

  @When("they hit the customer registration endpoint")
  public void registerACustomer() throws RestClientException {
    setCustomerResponseEntity(
        REST_CLIENT.sendRequest(
            getRegisterEndpoint(), HttpMethod.POST, customerPayload, Customer.class));
  }

  @Then("they receive their customer information back")
  public void validateCustomerInformationReceived() {
    Assert.assertNotNull(getCustomerResponseEntity());
    final Customer response = getCustomerResponseEntity().getBody();
    if (response != null) {
      customer.setPassword(null);
      Assertions.assertThat(customer)
          .usingRecursiveComparison()
          .ignoringFields("password, joinedOn, lastUpdatedOn")
          .isEqualTo(response);
      Assert.assertNull(response.getPassword());
      Assert.assertNotNull(response.getJoinedOn());
      Assert.assertNotNull(response.getLastUpdatedOn());
    } else {
      Assert.fail("Null response from custom registration endpoint!");
    }
  }
}
