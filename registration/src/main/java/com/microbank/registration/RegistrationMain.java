package com.microbank.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The MicroBank registration service.
 * Registers new users and bank accounts.
 */
@SpringBootApplication
public class RegistrationMain {

  public static void main(String[] args) {
    SpringApplication.run(RegistrationMain.class, args);
  }
}
