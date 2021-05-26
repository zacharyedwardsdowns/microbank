package com.microbank.customer;

import com.microbank.customer.model.Customer;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

public class ReusableCommonSteps extends CucumberBaseStep {

  @Given("a username for a registered user")
  public void getRegisteredUsername() throws IOException {
    final String customerJson = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerJson, Customer.class);
    Assert.assertNotNull(customer);
    Assert.assertNotNull(customer.getUsername());
  }

  @Then("they receive their customer information back")
  public void validateCustomerInformationReceived() {
    Assert.assertNotNull(customerResponseEntity);
    final Customer response = customerResponseEntity.getBody();
    if (response != null) {
      Assertions.assertThat(response)
          .usingRecursiveComparison()
          .ignoringFields("password", "joinedOn", "lastUpdatedOn", "customerId")
          .isEqualTo(customer);
      Assert.assertNull(response.getPassword());
      Assert.assertNotNull(response.getJoinedOn());
      Assert.assertNotNull(response.getCustomerId());
      Assert.assertNotNull(response.getLastUpdatedOn());
    } else {
      Assert.fail("Null response from the customer service!");
    }
    customerId = response.getCustomerId();
    customer = null;
  }

  @And("a status code of 200 is received")
  public void statusCode200() {
    Assert.assertNotNull(customerResponseEntity);
    Assert.assertEquals(HttpStatus.OK, customerResponseEntity.getStatusCode());
    customerResponseEntity = null;
  }
}
