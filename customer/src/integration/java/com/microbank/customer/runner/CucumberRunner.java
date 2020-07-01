package com.microbank.customer.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    glue = "com.microbank.customer.step",
    features = "src/integration/resources/feature",
    plugin = "html:build/cucumber/cucumber-html-report.html",
    tags = "@example")
public class CucumberRunner {}
