package com.microbank.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The MicroBank customer service.
 * Handles login, registration, and all data related to customers.
 */
@SpringBootApplication
public class CustomerMain {

  public static void main(String[] args) {
    SpringApplication.run(CustomerMain.class, args);
  }
}
