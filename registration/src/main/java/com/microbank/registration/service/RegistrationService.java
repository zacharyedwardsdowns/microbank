package com.microbank.registration.service;

import com.microbank.registration.model.BankAccount;
import com.microbank.registration.model.User;
import com.microbank.registration.repository.BankAccountRepository;
import com.microbank.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zachary Edwards Downs
 * A service to register users and bank accounts with MicroBank.
 */
@Service
public class RegistrationService {
  private BankAccountRepository bankAccountRepository;
  private UserRepository userRepository;

  /**
   * Injects the necessary dependencies.
   * @param bankAccountRepository A JpaRepository for bank accounts.
   * @param userRepository A JpaRepository for users.
   */
  @Autowired
  public RegistrationService(
    BankAccountRepository bankAccountRepository,
    UserRepository userRepository
  ) {
    this.bankAccountRepository = bankAccountRepository;
    this.userRepository = userRepository;
  }

  /**
   * Register a new user in the database.
   * @param user The user to register.
   * @return The information of the newly registered user.
   */
  public User registerUser(User user) {
    return this.userRepository.save(user);
  }

  /**
   * Register a new bank account in the database.
   * @param bankAccount The bank account to create.
   * @return The information of the newly registered bank account.
   */
  public BankAccount registerBankAccount(BankAccount bankAccount) {
    return this.bankAccountRepository.save(bankAccount);
  }
}
