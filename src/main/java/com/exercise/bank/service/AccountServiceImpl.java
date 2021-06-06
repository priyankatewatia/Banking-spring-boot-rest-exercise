package com.exercise.bank.service;

import com.exercise.bank.domain.Account;
import com.exercise.bank.exception.InternalServerException;
import com.exercise.bank.exception.ResourceNotFoundException;
import com.exercise.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public void deposit(int accountNumber, double amount) {
        Account account = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber)).orElseThrow(ResourceNotFoundException::new);
        account.setCurrentBalance(account.getCurrentBalance().add(BigDecimal.valueOf(amount)));
        try {
            accountRepository.save(account);
        }
        catch (Exception ex){
            throw new InternalServerException(ex.getMessage());
        }
    }

    public void withdraw(int accountNumber, double amount) {
        Account account = Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber)).orElseThrow(ResourceNotFoundException::new);
        account.setCurrentBalance(account.getCurrentBalance().subtract(BigDecimal.valueOf(amount)));
        try {
            accountRepository.save(account);
        }
        catch (Exception ex){
            throw new InternalServerException(ex.getMessage());
        }
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

}
