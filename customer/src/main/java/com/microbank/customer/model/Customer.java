package com.microbank.customer.model;

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

/**
 * A model to represent and transfer user data.
 */
@Table
@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Customer {
  @Id
  @Column(name = "username")
  private final String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "logged_in", nullable = false)
  private boolean loggedIn = false;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "date_of_birth", nullable = false)
  private Date dateOfBirth;

  @Column(name = "social_security_number", nullable = false)
  private String socialSecurityNumber;

  @Column(name = "joined_on", nullable = false)
  private final Date joinedOn;
}
