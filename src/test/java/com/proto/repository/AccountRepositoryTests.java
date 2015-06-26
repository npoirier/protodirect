package com.proto.repository;

import com.proto.ProtodirectApplication;
import com.proto.entity.Account;
import com.proto.entity.Subscription;
import com.proto.model.event.AccountStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByAccountIdentifier() throws Exception {
        Account account = new Account();
        account.setAccountIdentifier("dummy-identifier");
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);

        Account savedAccount = accountRepository.findOne("dummy-identifier");
        Assert.assertNotNull(savedAccount);
    }

    @Test
    public void embeddedOrder() throws Exception {
        Account account = new Account();
        account.setAccountIdentifier("dummy-identifier");
        account.setStatus(AccountStatus.ACTIVE);

        Subscription subscription = new Subscription();
        subscription.setEditionCode("dummy-edition");
        account.setSubscription(subscription);

        accountRepository.save(account);

        Account savedAccount = accountRepository.findOne("dummy-identifier");
        Assert.assertNotNull(savedAccount);
        Assert.assertNotNull(savedAccount.getSubscription());
    }
}
