package com.microbank.registration.model;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
 * A model to represent and transfer user data.
 */
public class User {
  @Transient
  private List<BankAccount> bankAccounts = new LinkedList<BankAccount>();

  @Id
  @Column(name = "username")
  private final String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "logged_in", nullable = false)
  private boolean loggedIn = false;
}
