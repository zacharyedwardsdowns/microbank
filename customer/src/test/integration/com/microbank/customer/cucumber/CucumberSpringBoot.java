package com.microbank.customer.cucumber;

import com.microbank.customer.Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(
    classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberSpringBoot {}
