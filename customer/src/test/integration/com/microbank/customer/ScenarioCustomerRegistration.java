package com.microbank.customer;

import com.microbank.customer.client.RestClient;
import com.microbank.customer.exception.RestClientException;
import com.microbank.customer.model.Customer;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.io.IOException;
import org.junit.Assert;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class ScenarioCustomerRegistration extends CucumberBaseStep {

  private static final RestClient REST_CLIENT = new RestClient();
  private String customerPayload;

  @Given("a user's registration information")
  public void getRegistrationInformation() throws IOException {
    customerPayload = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerPayload, Customer.class);
    Assert.assertNotNull(customer);
  }

  @When("they hit the customer registration endpoint")
  public void registerACustomer() throws RestClientException {
    customerResponseEntity =
        REST_CLIENT.sendRequest(
            getRegisterEndpoint(), HttpMethod.POST, customerPayload, Customer.class);
  }

  @And("a status code of 201 is received")
  public void statusCode201() {
    Assert.assertNotNull(customerResponseEntity);
    Assert.assertEquals(HttpStatus.CREATED, customerResponseEntity.getStatusCode());
    customerResponseEntity = null;
  }
}
