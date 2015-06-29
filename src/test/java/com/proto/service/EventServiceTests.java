package com.proto.service;

import com.proto.ProtodirectApplication;
import com.proto.client.AppDirectClient;
import com.proto.entity.*;
import com.proto.exception.UserAlreadyExistsException;
import com.proto.model.event.*;
import com.proto.model.event.Account;
import com.proto.model.event.Marketplace;
import com.proto.model.event.result.ErrorCode;
import com.proto.model.event.result.Result;
import com.proto.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
public class EventServiceTests {
    public static final String CREATOR_ID = "ec5d8eda-5cec-444d-9e30-125b6e4b67e2";
    public static final String ACCOUNT_ID = "ACCOUNT-ID";
    public static final String USER_ID = "USER-ID";

    public static final String EVENT_URL = "https://www.appdirect.com/dummyUrl";

    @Autowired
    @InjectMocks
    private EventService eventService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @Mock
    private AppDirectClient appDirectClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Checks that a UserAlreadyExistsException is thrown when trying to subscribe with an existing user.
     *
     * @throws UserAlreadyExistsException
     */
    @Test(expected = UserAlreadyExistsException.class)
    public void subscribeUserAlreadyExists() throws Exception {
        Event event = getEvent(Event.EventType.SUBSCRIPTION_ORDER);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);
        when(accountService.createAccount(any(Event.class))).thenReturn(new com.proto.entity.Account());
        when(userService.createUser(any(User.class), anyString())).thenThrow(UserAlreadyExistsException.class);

        eventService.subscribe(EVENT_URL);
    }

    /**
     * Given: an empty database
     * When:  unsubscribe using an unknown account identifier
     * Then:  result is success
     *
     * @throws Exception
     */
    @Test
    public void unsubscribeInexistantAccount() throws Exception {
        Event event = getEvent(Event.EventType.SUBSCRIPTION_CANCEL);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);

        eventService.unsubscribe(EVENT_URL);

        verify(userService).deleteByAccountIdentifier(anyString());
        verify(accountService).deleteAccount(anyString());
    }

    @Test
    public void change() throws Exception {
        Event event = getEvent(Event.EventType.SUBSCRIPTION_CHANGE);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);

        eventService.change(EVENT_URL);

        verify(accountService).updateSubscription(eq(ACCOUNT_ID), any(Subscription.class));
    }

    @Test
    public void status() throws Exception {
        Event event = getEvent(Event.EventType.SUBSCRIPTION_NOTICE);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);

        eventService.status(EVENT_URL);

        verify(accountService).updateAccountStatus(eq(ACCOUNT_ID), eq(AccountStatus.FREE_TRIAL));
    }

    @Test
    public void assign() throws Exception {
        Event event = getEvent(Event.EventType.USER_ASSIGNMENT);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);
        when(accountService.getAccountById(eq(ACCOUNT_ID))).thenReturn(new com.proto.entity.Account());
        eventService.assign(EVENT_URL);

        verify(userService).createUser(any(User.class), eq(ACCOUNT_ID));
    }

    @Test
    public void assignAccountNotFound() throws Exception {
        Event event = getEvent(Event.EventType.USER_ASSIGNMENT);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);
        when(accountService.getAccountById(eq(ACCOUNT_ID))).thenReturn(null);
        Result result = eventService.assign(EVENT_URL);

        assertEquals(result.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Test
    public void unassign() throws Exception {
        Event event = getEvent(Event.EventType.USER_UNASSIGNMENT);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);
        when(accountService.getAccountById(eq(ACCOUNT_ID))).thenReturn(new com.proto.entity.Account());
        when(userService.findByUuid(eq(USER_ID))).thenReturn(new AppUser());

        eventService.unassign(EVENT_URL);

        verify(userService).deleteByUuid(eq(USER_ID));
    }

    @Test
    public void unassignAccountNotFound() throws Exception {
        Event event = getEvent(Event.EventType.USER_UNASSIGNMENT);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);
        when(accountService.getAccountById(eq(ACCOUNT_ID))).thenReturn(null);

        Result result = eventService.unassign(EVENT_URL);

        assertEquals(result.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Test
    public void unassignUserNotFound() throws Exception {
        Event event = getEvent(Event.EventType.USER_UNASSIGNMENT);

        when(appDirectClient.getEvent(EVENT_URL)).thenReturn(event);
        when(accountService.getAccountById(eq(ACCOUNT_ID))).thenReturn(new com.proto.entity.Account());
        when(userService.findByUuid(eq(USER_ID))).thenReturn(null);

        Result result = eventService.unassign(EVENT_URL);

        assertEquals(result.getErrorCode(), ErrorCode.USER_NOT_FOUND);
    }

    private Event getEvent(Event.EventType eventType) {
        User creator = new User();
        String uuid = CREATOR_ID;
        creator.setUuid(uuid);
        Event event = new Event();
        event.setType(eventType);
        event.setCreator(creator);
        Payload payload = new Payload();
        event.setPayload(payload);
        Order order = new Order();
        payload.setOrder(order);
        Marketplace marketplace = new Marketplace();
        event.setMarketplace(marketplace);
        Account account = new Account();
        account.setAccountIdentifier(ACCOUNT_ID);
        account.setStatus(AccountStatus.FREE_TRIAL);
        payload.setAccount(account);
        Notice notice = new Notice();
        notice.setType(NoticeType.CLOSED);
        payload.setNotice(notice);
        User user = new User();
        user.setUuid(USER_ID);
        payload.setUser(user);

        return event;
    }

}
