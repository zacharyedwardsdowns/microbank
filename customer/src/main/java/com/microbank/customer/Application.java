package com.microbank.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The MicroBank customer service. Handles login, registration, and all data related to customers.
 */
@SpringBootApplication
public class Application {

  /**
   * Start the application.
   *
   * @param args Application arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
