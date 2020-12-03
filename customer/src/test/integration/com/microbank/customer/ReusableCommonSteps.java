package com.microbank.customer;

import io.cucumber.java.en.And;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

public class ReusableCommonSteps extends CucumberBaseStep {

  @And("a status code of 201 is received")
  public void statusCode201() {
    Assert.assertNotNull(getCustomerResponseEntity());
    Assert.assertEquals(HttpStatus.CREATED, getCustomerResponseEntity().getStatusCode());
    setCustomerResponseEntity(null);
  }
}
