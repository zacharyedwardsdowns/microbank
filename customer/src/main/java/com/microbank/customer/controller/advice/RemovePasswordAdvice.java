package com.microbank.customer.controller.advice;

import com.microbank.customer.model.Customer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RemovePasswordAdvice implements ResponseBodyAdvice<Customer> {
  @Override
  public boolean supports(
      @NonNull final MethodParameter returnType,
      @Nullable final Class<? extends HttpMessageConverter<?>> converterType) {
    final ResolvableType targetType = ResolvableType.forMethodParameter(returnType).getGeneric(0);
    return targetType.getType().getTypeName().equals("com.microbank.customer.model.Customer");
  }

  @Override
  public Customer beforeBodyWrite(
      final Customer body,
      @Nullable final MethodParameter returnType,
      @Nullable final MediaType selectedContentType,
      @Nullable final Class<? extends HttpMessageConverter<?>> selectedConverterType,
      @Nullable final ServerHttpRequest request,
      @Nullable final ServerHttpResponse response) {
    if (body != null) {
      body.setPassword(null);
    }
    return body;
  }
}
