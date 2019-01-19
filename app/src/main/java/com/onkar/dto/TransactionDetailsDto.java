package com.onkar.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionDetailsDto {

    @SerializedName("userDto")
    private UserDto userDto;

    @SerializedName("history")
    private List<MonthSpendingDto> history;

    public TransactionDetailsDto(final UserDto userDto,
                                 final List<MonthSpendingDto> history) {
        super();
        this.userDto = userDto;
        this.history = history;
    }

    public List<MonthSpendingDto> getHistory() {
        return history;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setHistory(final List<MonthSpendingDto> history) {
        this.history = history;
    }

    public void setUserDto(final UserDto userDto) {
        this.userDto = userDto;
    }
}
