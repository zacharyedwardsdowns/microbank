package com.microbank.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
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
  @BsonId @JsonIgnore private ObjectId id;

  @JsonProperty("Username")
  @BsonProperty("Username")
  private String username;

  @JsonProperty("Password")
  @BsonProperty("Password")
  private String password;

  @JsonProperty("FirstName")
  @BsonProperty("FirstName")
  private String firstName;

  @JsonProperty("MiddleName")
  @BsonProperty("MiddleName")
  private String middleName;

  @JsonProperty("LastName")
  @BsonProperty("LastName")
  private String lastName;

  @JsonProperty("Suffix")
  @BsonProperty("Suffix")
  private String suffix;

  @JsonProperty("Gender")
  @BsonProperty("Gender")
  private String gender;

  @JsonProperty("Address")
  @BsonProperty("Address")
  private String address;

  @JsonProperty("City")
  @BsonProperty("City")
  private String city;

  @JsonProperty("State")
  @BsonProperty("State")
  private String state;

  @JsonProperty("PostalCode")
  @BsonProperty("PostalCode")
  private String postalCode;

  @JsonProperty("DateOfBirth")
  @BsonProperty("DateOfBirth")
  private LocalDate dateOfBirth;

  @JsonProperty("SocialSecurityNumber")
  @BsonProperty("SocialSecurityNumber")
  private String socialSecurityNumber;

  @JsonProperty("JoinedOn")
  @BsonProperty("JoinedOn")
  private LocalDateTime joinedOn;

  @JsonProperty("LastUpdatedOn")
  @BsonProperty("LastUpdatedOn")
  private LocalDateTime lastUpdatedOn;
}
