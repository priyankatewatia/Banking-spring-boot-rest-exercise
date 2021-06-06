package com.exercise.bank.service;

import com.exercise.bank.domain.Account;

import java.util.List;

public interface AccountService {
    void deposit(int accountNumber, double amount);

    void withdraw(int accountNumber, double amount) throws Exception;

    List<Account> getAllAccounts();
}
