package com.microbank.customer.cucumber;

import com.microbank.customer.model.Customer;
import com.microbank.customer.util.Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class ReusableCommonSteps extends CucumberBaseStep {

  @Given("a username for a registered user")
  public void getRegisteredUsername() throws IOException {
    final String customerJson = readFile("json/Customer.json");
    customer = Util.MAPPER.readValue(customerJson, Customer.class);
    Assertions.assertNotNull(customer);
    Assertions.assertNotNull(customer.getUsername());
  }

  @Then("they receive their customer information back")
  public void validateCustomerInformationReceived() {
    Assertions.assertNotNull(customerResponseEntity);
    final Customer response = customerResponseEntity.getBody();
    if (response != null) {
      org.assertj.core.api.Assertions.assertThat(response)
          .usingRecursiveComparison()
          .ignoringFields("password", "joinedOn", "lastUpdatedOn", "customerId")
          .isEqualTo(customer);
      Assertions.assertNull(response.getPassword());
      Assertions.assertNotNull(response.getJoinedOn());
      Assertions.assertNotNull(response.getCustomerId());
      Assertions.assertNotNull(response.getLastUpdatedOn());
    } else {
      Assertions.fail("Null response from the customer service!");
    }
    customerId = response.getCustomerId();
    customer = null;
  }

  @And("a status code of 200 is received")
  public void statusCode200() {
    Assertions.assertNotNull(customerResponseEntity);
    Assertions.assertEquals(HttpStatus.OK, customerResponseEntity.getStatusCode());
    customerResponseEntity = null;
  }
}
