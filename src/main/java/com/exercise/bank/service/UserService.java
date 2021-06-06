package com.exercise.bank.service;

import com.exercise.bank.domain.User;

public interface UserService {

    User createAccount(String userName);

    User findByUserName(String userName);

}
