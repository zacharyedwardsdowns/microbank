package com.microbank.registration.repository;

import com.microbank.registration.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A JpaRepository for saving bank account information.
 */
@Repository
public interface BankAccountRepository
  extends JpaRepository<BankAccount, String> {}
