package com.microbank.customer.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** A model to represent and transfer tokens. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {

  @JsonProperty("IdToken")
  private String idToken;

  @JsonProperty("AccessToken")
  private String accessToken;

  @JsonProperty("RefreshToken")
  private String refreshToken;
}
