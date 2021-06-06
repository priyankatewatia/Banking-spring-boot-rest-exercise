package com.exercise.bank.test;

import com.exercise.bank.domain.Account;
import com.exercise.bank.domain.User;
import com.exercise.bank.repository.UserRepository;
import com.exercise.bank.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void testFindByUserName() {
        User user = new User("priya");
        when(userRepository.findByUserName("priya")).thenReturn(user);
        assertEquals(user, userService.findByUserName("priya"));
    }

    @Test()
    public void testCreateAccountForSuccess() {
        Account account = new Account(BigDecimal.valueOf(300.0));
        account.setAccountNumber(1234567);
        User user = new User("priya");
        user.setAccount(account);
        when(userRepository.findByUserName("priya")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.createAccount("priya");
        assertEquals(user, result);
    }
}
