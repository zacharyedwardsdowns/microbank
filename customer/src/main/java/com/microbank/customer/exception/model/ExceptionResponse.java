package com.microbank.customer.exception.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** A model to represent the controller response when an exception occurs. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
  private String timestamp;
  private Integer status;
  private String error;
  private String message;
  private String path;
  private ExceptionCause cause;
}
