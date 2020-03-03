package com.microbank.registration.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table
@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@AllArgsConstructor
/**
 * A model to represent and transfer bank account data.
 */
public class BankAccount {
  @Id
  @Column(name = "account_number")
  private final String accountNumber;

  @Column(name = "balance", nullable = false, scale = 2)
  private double balance;

  @Column(name = "active", nullable = false)
  private boolean active;

  @Column(name = "created_on", nullable = false)
  private Date createdOn;
}
