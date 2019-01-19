package com.onkar.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserDto implements Serializable {

  private static final long serialVersionUID = 6352283278289363880L;

  @SerializedName("username")
  private String username;

  @SerializedName("cardType")
  private String cardType;

  @SerializedName("limit")
  private BigDecimal limit;

  public UserDto(String username, String cardType, BigDecimal limit) {
    this.username = username;
    this.cardType = cardType;
    this.limit = limit;
  }

  public String getCardType() {
    return cardType;
  }

  public BigDecimal getLimit() {
    return limit;
  }

  public String getUsername() {
    return username;
  }

  public void setCardType(final String cardType) {
    this.cardType = cardType;
  }

  public void setLimit(final BigDecimal limit) {
    this.limit = limit;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

}
