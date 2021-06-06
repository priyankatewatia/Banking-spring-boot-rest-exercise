package com.exercise.bank.service;

import com.exercise.bank.domain.Account;
import com.exercise.bank.domain.User;
import com.exercise.bank.exception.InternalServerException;
import com.exercise.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private static int accountNumberSeries = 100000;

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User createAccount(String userName) {
        User user = new User(userName);
        Account account = new Account(BigDecimal.ZERO);
        account.setAccountNumber(generateAccountNumber());
        user.setAccount(account);

        try {
            userRepository.save(user);
        }
        catch (Exception ex){
            throw new InternalServerException(ex.getMessage());
        }
        return userRepository.findByUserName(user.getUserName());
    }

    private int generateAccountNumber() {
        return ++accountNumberSeries;
    }
}
