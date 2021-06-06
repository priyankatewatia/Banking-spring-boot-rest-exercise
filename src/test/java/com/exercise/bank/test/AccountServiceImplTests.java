package com.exercise.bank.test;

import com.exercise.bank.domain.Account;
import com.exercise.bank.domain.User;
import com.exercise.bank.exception.InternalServerException;
import com.exercise.bank.repository.AccountRepository;
import com.exercise.bank.service.AccountServiceImpl;
import org.h2.message.DbException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceImplTests {
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    public void testGetAllAccounts() {
        Account account1 = new Account(BigDecimal.valueOf(200.0));
        Account account2 = new Account(BigDecimal.valueOf(300.0));
        List<Account> accountList = Arrays.asList(account1, account2);
        when(accountRepository.findAll()).thenReturn(accountList);
        assertEquals(accountList, accountService.getAllAccounts());
        assertEquals(2, accountList.size());
    }

    @Test(expected = InternalServerException.class)
    public void testDepositForFailure() {
        Account account = new Account(BigDecimal.valueOf(300.0));
        account.setAccountNumber(1234567);
        User user = new User("priya");
        user.setAccount(account);
        when(accountRepository.findByAccountNumber(1234567)).thenReturn(account);
        account.setCurrentBalance(BigDecimal.valueOf(300.0));
        when(accountRepository.save(account)).thenThrow(DbException.class);
        accountService.deposit(1234567, 300.0);
    }
}
