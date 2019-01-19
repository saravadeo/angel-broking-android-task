package com.onkar.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MonthSpendingDto implements Serializable {

  private static final long serialVersionUID = 1350661983642321956L;

  @SerializedName("month")
  private String month;

  @SerializedName("year")
  private int year;

  @SerializedName("totalSpent")
  private BigDecimal totalSpent;

  @SerializedName("spentOn")
  private Map<String, BigDecimal> spentOn = new HashMap<>();

  @SerializedName("currentMonth")
  private boolean currentMonth;

  public MonthSpendingDto(String month,int year, BigDecimal totalSpent, Map<String, BigDecimal> spentOn, boolean currentMonth) {
    this.month = month;
    this.year = year;
    this.totalSpent = totalSpent;
    this.spentOn = spentOn;
    this.currentMonth = currentMonth;
  }

  public String getMonth() {
    return month;
  }

  public Map<String, BigDecimal> getSpentOn() {
    return spentOn;
  }

  public BigDecimal getTotalSpent() {
    return totalSpent;
  }

  public boolean isCurrentMonth() {
    return currentMonth;
  }

  public void setCurrentMonth(final boolean currentMonth) {
    this.currentMonth = currentMonth;
  }

  public void setMonth(final String month) {
    this.month = month;
  }

  public void setSpentOn(final Map<String, BigDecimal> spentOn) {
    this.spentOn = spentOn;
  }

  public void setTotalSpent(final BigDecimal totalSpent) {
    this.totalSpent = totalSpent;
  }

  public int getYear() {
    return this.year;
  }

  public void setYear(int year) {
    this.year = year;
  }
}
