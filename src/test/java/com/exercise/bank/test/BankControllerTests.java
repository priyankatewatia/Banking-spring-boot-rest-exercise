package com.exercise.bank.test;

import com.exercise.bank.controller.BankController;
import com.exercise.bank.domain.Account;
import com.exercise.bank.domain.User;
import com.exercise.bank.domain.UserRequest;
import com.exercise.bank.exception.LowFundsException;
import com.exercise.bank.exception.ResourceNotFoundException;
import com.exercise.bank.service.AccountServiceImpl;
import com.exercise.bank.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = BankController.class)
public class BankControllerTests {
    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private AccountServiceImpl accountService;

    @MockBean
    private UserServiceImpl userService;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void testDepositAmount() throws Exception {
        Account account = new Account(BigDecimal.valueOf(300.0));
        User user = new User("priya");
        user.setAccount(account);

        UserRequest request = new UserRequest();
        request.setUserName("priya");
        request.setAmount(200.0);

        String inputJson = mapToJson(request);
        when(userService.findByUserName("priya")).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/bank/deposit").contentType(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(201, status);
        String actual = result.getResponse().getContentAsString();
        assertEquals("Amount is successfully deposited", actual);
    }

    @Test
    public void testDepositAmountForNewUser() throws Exception {
        Account account = new Account(BigDecimal.ZERO);
        account.setAccountNumber(100001);
        User user = new User("priya");
        user.setAccount(account);

        UserRequest request = new UserRequest();
        request.setUserName("priya");
        request.setAmount(200.0);

        String inputJson = mapToJson(request);
        when(userService.findByUserName("priya")).thenReturn(null);
        when(userService.createAccount("priya")).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/bank/deposit").contentType(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(201, status);
        String actual = result.getResponse().getContentAsString();
        assertEquals("Amount is successfully deposited", actual);
    }

    @Test
    public void testDepositAmountForNewUserFailure() throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName("priya");
        request.setAmount(200.0);

        String inputJson = mapToJson(request);
        when(userService.findByUserName("priya")).thenReturn(null);
        when(userService.createAccount("priya")).thenReturn(null);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/bank/deposit").contentType(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();
        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException);
        int status = result.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void testWithdrawAmount() throws Exception {
        Account account = new Account(BigDecimal.valueOf(300.0));
        User user = new User("priya");
        user.setAccount(account);

        UserRequest request = new UserRequest();
        request.setUserName("priya");
        request.setAmount(200.0);

        String inputJson = mapToJson(request);
        when(userService.findByUserName("priya")).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/bank/withdraw").contentType(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        String actual = result.getResponse().getContentAsString();
        assertEquals("Amount is successfully withdrawn", actual);
    }

    @Test
    public void testWithdrawAmountForLowFundsException() throws Exception {
        Account account = new Account(BigDecimal.valueOf(300.0));
        User user = new User("priya");
        user.setAccount(account);

        UserRequest request = new UserRequest();
        request.setUserName("priya");
        request.setAmount(400.0);

        String inputJson = mapToJson(request);
        when(userService.findByUserName("priya")).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/bank/withdraw").contentType(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();
        assertTrue(result.getResolvedException() instanceof LowFundsException);
        int status = result.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    public void testWithdrawAmountForNoUserFound() throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName("priya");
        request.setAmount(400.0);

        String inputJson = mapToJson(request);
        when(userService.findByUserName("priya")).thenReturn(null);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/bank/withdraw").contentType(MediaType.APPLICATION_JSON).content(inputJson))
                .andReturn();
        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException);
        int status = result.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void testGetCustomerBalance() throws Exception {
        Account account = new Account(BigDecimal.valueOf(300.0));
        User user = new User("priya");
        user.setAccount(account);

        when(userService.findByUserName("priya")).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/bank/getCustomerBalance/priya").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        BigDecimal expected = BigDecimal.valueOf(300.0);
        String actual = result.getResponse().getContentAsString();
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testGetCustomerBalanceForException() throws Exception {
        Account account = new Account(BigDecimal.valueOf(300.0));
        User user = new User("priya");
        user.setAccount(account);

        when(userService.findByUserName("priya")).thenReturn(null);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/bank/getCustomerBalance/priya").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException);
        int status = result.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void testGetTotalBankBalance() throws Exception {
        List<Account> accountList = Arrays.asList(new Account(BigDecimal.valueOf(200.0)), new Account(BigDecimal.valueOf(400.0)));
        when(accountService.getAllAccounts()).thenReturn(accountList);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/bank/getTotalBankBalance").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        BigDecimal expected = BigDecimal.valueOf(600.0);
        String actual = result.getResponse().getContentAsString();
        assertEquals(expected.toString(), actual);
    }
}
