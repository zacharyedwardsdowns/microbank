package com.microbank.registration.controller;

import com.microbank.registration.model.BankAccount;
import com.microbank.registration.model.User;
import com.microbank.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zachary Edwards Downs
 * A controller to register users and bank accounts with MicroBank.
 */
@RestController
public class RegistrationController {
  private RegistrationService registrationService;

  /**
   * Injects the necessary dependencies.
   * @param registrationService A service for the business logic of registration.
   */
  @Autowired
  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  /**
   * Register a new user in the database.
   * @param user The user to register.
   * @return The information of the newly registered user.
   */
  @PostMapping("/register-user")
  public User registerUser(@RequestBody User user) {
    return registrationService.registerUser(user);
  }

  /**
   * Register a new bank account in the database.
   * @param bankAccount The bank account to create.
   * @return The information of the newly registered bank account.
   */
  @PostMapping("/register-bank-account")
  public BankAccount registerBankAccount(@RequestBody BankAccount bankAccount) {
    return registrationService.registerBankAccount(bankAccount);
  }
}
