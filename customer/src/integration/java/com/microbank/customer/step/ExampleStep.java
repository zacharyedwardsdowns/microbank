package com.microbank.customer.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class ExampleStep {

  private boolean bool;

  @Given("scenario")
  public void given() {
    this.bool = true;
  }

  @Then("test")
  public void then() {
    Assert.assertTrue(this.bool);
  }
}
