package com.microbank.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** A model to represent and transfer customer data. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Customer")
public class Customer {

  @Id
  @JsonProperty("CustomerId")
  private String customerId;

  @JsonProperty("Username")
  private String username;

  @JsonProperty("Password")
  private String password;

  @JsonProperty("FirstName")
  private String firstName;

  @JsonProperty("MiddleName")
  private String middleName;

  @JsonProperty("LastName")
  private String lastName;

  @JsonProperty("Suffix")
  private String suffix;

  @JsonProperty("Gender")
  private String gender;

  @JsonProperty("Address")
  private String address;

  @JsonProperty("City")
  private String city;

  @JsonProperty("State")
  private String state;

  @JsonProperty("PostalCode")
  private String postalCode;

  @JsonProperty("DateOfBirth")
  private LocalDate dateOfBirth;

  @JsonProperty("SocialSecurityNumber")
  private String socialSecurityNumber;

  @JsonProperty("JoinedOn")
  private Instant joinedOn;

  @JsonProperty("LastUpdatedOn")
  private Instant lastUpdatedOn;
}
