package com.microbank.customer.cucumber;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("feature")
@ConfigurationParameter( //
    key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME,
    value = "true")
@ConfigurationParameter( //
    key = Constants.PLUGIN_PROPERTY_NAME,
    value = "html:build/cucumber/cucumber.html")
@ConfigurationParameter( //
    key = Constants.GLUE_PROPERTY_NAME,
    value = "com.microbank.customer.cucumber")
@ConfigurationParameter( //
    key = Constants.FILTER_TAGS_PROPERTY_NAME,
    value = "@Initial")
public class CucumberTestRunner {}
