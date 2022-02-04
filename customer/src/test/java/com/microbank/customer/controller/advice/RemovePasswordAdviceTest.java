package com.microbank.customer.controller.advice;

import com.microbank.customer.controller.CustomerController;
import com.microbank.customer.model.Customer;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;

class RemovePasswordAdviceTest {
  final RemovePasswordAdvice passwordAdvice = new RemovePasswordAdvice();

  @Test
  void supports() throws Exception {
    final Method method = CustomerController.class.getMethod("authorize", String.class);
    final MethodParameter methodParameter = new MethodParameter(method, 0);
    Assertions.assertFalse(passwordAdvice.supports(methodParameter, null));
  }

  @Test
  void beforeBodyWrite() {
    final Customer customer = new Customer();
    customer.setPassword("");
    final Customer response =
        passwordAdvice.beforeBodyWrite(customer, null, null, null, null, null);
    Assertions.assertNotNull(response);
    Assertions.assertNull(response.getPassword());
  }

  @Test
  void beforeBodyWriteNull() {
    Assertions.assertNull(passwordAdvice.beforeBodyWrite(null, null, null, null, null, null));
  }
}
