package com.proto.controller;

import com.proto.entity.Account;
import com.proto.entity.AppUser;
import com.proto.service.AccountService;
import com.proto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 */
@Controller
public class HomeController {
    private static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    /**
     * Displays the current user, account details, and list of account users
     *
     * @param model
     * @param authentication
     * @return
     */
    @RequestMapping("/")
    public String index(Model model, OpenIDAuthenticationToken authentication) {
        AppUser currentUser = userService.findByAuthenticationToken(authentication);

        List<AppUser> accountUsers = userService.findByAccountIdentifier(currentUser.getAccountIdentifier());
        Account account = accountService.getAccountById(currentUser.getAccountIdentifier());

        model.addAttribute("account", account);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("accountUsers", accountUsers);

        model.addAttribute("authentication", authentication);

        return "index";
    }

    /**
     * Handles RuntimeException to return the appropriate error.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleRuntimeException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.addObject("exception", ex);
        mav.setViewName("error");
        return mav;
    }
}
