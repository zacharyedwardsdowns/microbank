package com.microbank.customer;

import com.microbank.customer.model.Customer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

public class ReusableCommonSteps extends CucumberBaseStep {

  @Then("they receive their customer information back")
  public void validateCustomerInformationReceived() {
    Assert.assertNotNull(customerResponseEntity);
    final Customer response = customerResponseEntity.getBody();
    if (response != null) {
      Assertions.assertThat(customer)
          .usingRecursiveComparison()
          .ignoringFields("password", "joinedOn", "lastUpdatedOn")
          .isEqualTo(response);
      Assert.assertNull(response.getPassword());
      Assert.assertNotNull(response.getJoinedOn());
      Assert.assertNotNull(response.getLastUpdatedOn());
    } else {
      Assert.fail("Null response from the customer service!");
    }
    customer = null;
  }

  @And("a status code of 201 is received")
  public void statusCode201() {
    Assert.assertNotNull(customerResponseEntity);
    Assert.assertEquals(HttpStatus.CREATED, customerResponseEntity.getStatusCode());
    customerResponseEntity = null;
  }

  @And("a status code of 200 is received")
  public void statusCode200() {
    Assert.assertNotNull(customerResponseEntity);
    Assert.assertEquals(HttpStatus.OK, customerResponseEntity.getStatusCode());
    customerResponseEntity = null;
  }
}
