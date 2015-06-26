package com.proto.controller;

import com.proto.ProtodirectApplication;
import com.proto.entity.Account;
import com.proto.entity.AppUser;
import com.proto.service.AccountService;
import com.proto.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
@WebAppConfiguration
public class HomeControllerTests {
    @InjectMocks
    HomeController homeController;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void testIndex200() throws Exception {
        AppUser user = new AppUser();
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setAccountIdentifier("dummy-account");

        when(userService.findByAuthenticationToken(null)).thenReturn(user);
        when(accountService.getAccountById("dummy-account")).thenReturn(new Account());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
