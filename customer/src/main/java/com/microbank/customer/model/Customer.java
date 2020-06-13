package com.microbank.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
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

/**
 * A model to represent and transfer user data.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Customer")
public class Customer {
  @BsonId
  @JsonIgnore
  private ObjectId id;

  @JsonProperty("Username")
  @BsonProperty("Username")
  private String username;

  @JsonProperty("Password")
  @BsonProperty("Password")
  private String password;

  @JsonProperty("LoggedIn")
  @BsonProperty("LoggedIn")
  private boolean loggedIn = false;

  @JsonProperty("Address")
  @BsonProperty("Address")
  private String address;

  @JsonProperty("DateOfBirth")
  @BsonProperty("DateOfBirth")
  private Date dateOfBirth;

  @JsonProperty("SocialSecurityNumber")
  @BsonProperty("SocialSecurityNumber")
  private String socialSecurityNumber;

  @JsonProperty("JoinedOn")
  @BsonProperty("JoinedOn")
  private Date joinedOn;
}
