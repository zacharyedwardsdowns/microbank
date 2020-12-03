package com.microbank.customer;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    glue = "com.microbank.customer",
    features = "src/test/integration/com/microbank/customer/feature",
    plugin = "html:build/cucumber/cucumber-html-report.html",
    tags = "@Initial")
public class CucumberTestRunner {}
