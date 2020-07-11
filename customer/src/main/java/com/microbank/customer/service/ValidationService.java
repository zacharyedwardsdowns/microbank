package com.microbank.customer.service;

import com.microbank.customer.exception.ValidationException;
import com.microbank.customer.exception.validation.InvalidUsernameException;
import com.microbank.customer.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private static final String USERNAME_REGEX = "^[A-Za-z0-9]{6,30}$";

  public void validateNewCustomer(final Customer customer) throws ValidationException {

    if (customer.getUsername() == null) {
      throw new InvalidUsernameException("Username cannot be null!");
    } else if (!customer.getUsername().matches(USERNAME_REGEX)) {
      throw new InvalidUsernameException(
          "Invalid username! Must be 6-30 characters long and only contain alphanumeric symbols.");
    }
  }
}
