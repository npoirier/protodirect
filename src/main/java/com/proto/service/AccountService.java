package com.proto.service;

import com.proto.entity.Account;
import com.proto.entity.Marketplace;
import com.proto.entity.Subscription;
import com.proto.model.event.AccountStatus;
import com.proto.model.event.Event;
import com.proto.repository.AccountRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 *
 */
@Service
public class AccountService {
    private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Mapper mapper;

    public Account getAccountById(String accountIdentifier) {
        return accountRepository.findOne(accountIdentifier);
    }

    /**
     * Deletes an account
     *
     * @param accountIdentifier
     */
    public void deleteAccount(String accountIdentifier) {
        if (accountRepository.exists(accountIdentifier)) {
            accountRepository.delete(accountIdentifier);
        } else {
            LOGGER.warn("Account does not exist {}", accountIdentifier);
        }
    }

    /**
     * Creates and saves a new account from an event
     *
     * @param event
     * @return
     */
    public Account createAccount(Event event) {
        Account account = new Account();
        account.setAccountIdentifier(UUID.randomUUID().toString());
        account.setStatus(AccountStatus.ACTIVE);

        Subscription subscription = mapper.map(event.getPayload().getOrder(), Subscription.class);
        account.setSubscription(subscription);

        Marketplace marketplace = mapper.map(event.getMarketplace(), Marketplace.class);
        account.setMarketplace(marketplace);

        return accountRepository.save(account);
    }

    /**
     * Update an account's subscription
     *
     * @param accountIdentifier
     * @param subscription
     */
    public void updateSubscription(String accountIdentifier, Subscription subscription) {
        Account account = accountRepository.findOne(accountIdentifier);
        account.setSubscription(subscription);
        accountRepository.save(account);
    }

    /**
     * Update an account's status
     */
    public void updateAccountStatus(String accountIdentifier, AccountStatus status) {
        Account account = accountRepository.findOne(accountIdentifier);
        account.setStatus(status);
        accountRepository.save(account);
    }
}
