package com.exercise.bank.domain;

import javax.validation.constraints.NotNull;

public class UserRequest {
    @NotNull
    public String userName;

    @NotNull
    public double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
