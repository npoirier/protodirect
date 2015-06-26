package com.proto.service;

import com.google.common.base.Preconditions;
import com.proto.client.AppDirectClient;
import com.proto.entity.Account;
import com.proto.entity.AppUser;
import com.proto.entity.Subscription;
import com.proto.exception.UserAlreadyExistsException;
import com.proto.model.event.Event;
import com.proto.model.event.NoticeType;
import com.proto.model.event.result.ErrorCode;
import com.proto.model.event.result.Result;
import com.proto.model.event.result.ResultBuilder;
import oauth.signpost.exception.OAuthException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

/**
 * Handles the create, cancel, change and status operations on accounts subscriptions
 */
@Service
public class EventService {
    private static Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppDirectClient appDirectClient;

    @Autowired
    private Mapper mapper;

    /**
     * Creates a new subscription
     * <p>
     * http://info.appdirect.com/developers/docs/api_integration/subscription_management/subscription_order_event
     *
     * @param url
     * @return
     */
    public Result subscribe(String url) throws UserAlreadyExistsException, OAuthException, URISyntaxException {
        Event event = appDirectClient.getEvent(url);
        Preconditions.checkState(event.getType().equals(Event.EventType.SUBSCRIPTION_ORDER));

        Account account = accountService.createAccount(event);
        userService.createUser(event.getCreator(), account.getAccountIdentifier());

        return new ResultBuilder().setSuccess(true).setAccountIdentifier(account.getAccountIdentifier()).createResult();
    }

    /**
     * Cancel a subscription
     * <p>
     * http://info.appdirect.com/developers/docs/api_integration/subscription_management/subscription_cancel_event
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    public Result unsubscribe(String url) throws OAuthException, URISyntaxException {
        Event event = appDirectClient.getEvent(url);
        Preconditions.checkState(event.getType().equals(Event.EventType.SUBSCRIPTION_CANCEL));

        String accountIdentifier = event.getPayload().getAccount().getAccountIdentifier();

        userService.deleteByAccountIdentifier(accountIdentifier);
        accountService.deleteAccount(accountIdentifier);

        return new ResultBuilder().setSuccess(true).createResult();
    }

    /**
     * Modifies a client subscription with the event's order
     * <p>
     * http://info.appdirect.com/developers/docs/api_integration/subscription_management/subscription_change_event
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    public Result change(String url) throws OAuthException, URISyntaxException {
        Event event = appDirectClient.getEvent(url);
        Preconditions.checkState(event.getType().equals(Event.EventType.SUBSCRIPTION_CHANGE));

        Subscription subscription = mapper.map(event.getPayload().getOrder(), Subscription.class);

        if (Event.EventFlag.DEVELOPMENT == event.getFlag()
                && "dummy-account".equals(event.getPayload().getAccount().getAccountIdentifier())) {
            // For integration testing
            return new ResultBuilder().setSuccess(true).createResult();
        }

        String accountIdentifier = event.getPayload().getAccount().getAccountIdentifier();
        accountService.updateSubscription(accountIdentifier, subscription);

        return new ResultBuilder().setSuccess(true).createResult();
    }

    /**
     * Handles the subscription notice event.
     * <p>
     * Updates the account status on REACTIVATED, DEACTIVATED and CLOSED notifications.
     * <p>
     * http://info.appdirect.com/developers/docs/api_integration/subscription_management/subscription_notice_event
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    public Result status(String url) throws OAuthException, URISyntaxException {
        Event event = appDirectClient.getEvent(url);
        Preconditions.checkState(event.getType().equals(Event.EventType.SUBSCRIPTION_NOTICE));

        NoticeType type = event.getPayload().getNotice().getType();
        com.proto.model.event.Account account = event.getPayload().getAccount();

        if ("dummy-account".equals(account.getAccountIdentifier())) {
            // For integration testing
            return new ResultBuilder().setSuccess(true).createResult();
        }

        switch (type) {
            case DEACTIVATED:
            case REACTIVATED:
            case CLOSED:
                accountService.updateAccountStatus(account.getAccountIdentifier(), account.getStatus());
                break;
            case UPCOMING_INVOICE:
                break;
        }
        return new ResultBuilder().setSuccess(true).createResult();
    }

    /**
     * Handles the assign event notification to create a new user.
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     * @throws UserAlreadyExistsException
     */
    public Result assign(String url) throws OAuthException, URISyntaxException, UserAlreadyExistsException {
        Event event = appDirectClient.getEvent(url);
        Preconditions.checkState(event.getType().equals(Event.EventType.USER_ASSIGNMENT));

        String accountIdentifier = event.getPayload().getAccount().getAccountIdentifier();
        Account account = accountService.getAccountById(accountIdentifier);
        if (account == null) {
            return new ResultBuilder().setSuccess(false).setErrorCode(ErrorCode.ACCOUNT_NOT_FOUND).createResult();
        }

        userService.createUser(event.getPayload().getUser(), accountIdentifier);
        return new ResultBuilder().setSuccess(true).createResult();
    }

    /**
     * Handles the unassign event notification to delete a user.
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    public Result unassign(String url) throws OAuthException, URISyntaxException {
        Event event = appDirectClient.getEvent(url);
        Preconditions.checkState(event.getType().equals(Event.EventType.USER_UNASSIGNMENT));

        String accountIdentifier = event.getPayload().getAccount().getAccountIdentifier();
        Account account = accountService.getAccountById(accountIdentifier);
        if (account == null) {
            return new ResultBuilder().setSuccess(false).setErrorCode(ErrorCode.ACCOUNT_NOT_FOUND).createResult();
        }

        String uuid = event.getPayload().getUser().getUuid();
        AppUser user = userService.findByUuid(uuid);
        if (user == null) {
            return new ResultBuilder().setSuccess(false).setErrorCode(ErrorCode.USER_NOT_FOUND).createResult();
        }

        userService.deleteByUuid(uuid);
        return new ResultBuilder().setSuccess(true).createResult();
    }
}
