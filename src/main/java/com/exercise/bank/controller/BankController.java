package com.exercise.bank.controller;

import com.exercise.bank.domain.UserRequest;
import com.exercise.bank.domain.Account;
import com.exercise.bank.domain.User;
import com.exercise.bank.exception.LowFundsException;
import com.exercise.bank.exception.ResourceNotFoundException;
import com.exercise.bank.service.AccountServiceImpl;
import com.exercise.bank.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/bank-api/v1")
public class BankController {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/deposit", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> depositAmount(@RequestBody UserRequest userRequest) {
        String userName = userRequest.userName;
        User user = userService.findByUserName(userName);
        if (user != null) {
            Account account = user.getAccount();
            accountService.deposit(account.getAccountNumber(), userRequest.amount);
        }
        else
        {
            User createdUser = Optional.ofNullable(userService.createAccount(userName)).orElseThrow(ResourceNotFoundException::new);
            Account account = createdUser.getAccount();
            accountService.deposit(account.getAccountNumber(), userRequest.amount);
        }
        return new ResponseEntity<>("Amount is successfully deposited", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> withdrawAmount(@RequestBody UserRequest userRequest) {
        String userName = userRequest.userName;
        User user = Optional.ofNullable(userService.findByUserName(userName)).orElseThrow(ResourceNotFoundException::new);
        Account account = user.getAccount();
        if(account.getCurrentBalance().compareTo(BigDecimal.valueOf(userRequest.amount)) < 0) {
            throw new LowFundsException();
        }
        else {
            accountService.withdraw(account.getAccountNumber(), userRequest.amount);
            return new ResponseEntity<>("Amount is successfully withdrawn", HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getCustomerBalance/{userName}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BigDecimal> getCustomerBalance(@PathVariable("userName") String userName) {
        User user = Optional.ofNullable(userService.findByUserName(userName)).orElseThrow(ResourceNotFoundException::new);
        Account account = user.getAccount();
        BigDecimal balance = account.getCurrentBalance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @RequestMapping(value = "/getTotalBankBalance", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<BigDecimal> getTotalBankBalance() {
        List<Account> accounts = accountService.getAllAccounts();
        List<BigDecimal> balanceAmountList = accounts.stream().map(Account::getCurrentBalance).collect(Collectors.toList());
        return new ResponseEntity<>(balanceAmountList.stream().reduce(BigDecimal.ZERO, BigDecimal::add), HttpStatus.OK);
    }
}
