package com.proto.service;

import com.proto.ProtodirectApplication;
import com.proto.client.AppDirectClient;
import com.proto.entity.*;
import com.proto.exception.UserAlreadyExistsException;
import com.proto.model.event.*;
import com.proto.model.event.Account;
import com.proto.model.event.Marketplace;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
public class EventServiceTests {
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

        String eventUrl = "https://www.appdirect.com/dummyUrl";
        when(appDirectClient.getEvent(eventUrl)).thenReturn(event);
        when(accountService.createAccount(any(Event.class))).thenReturn(new com.proto.entity.Account());
        when(userService.createUser(any(User.class), anyString())).thenThrow(UserAlreadyExistsException.class);

        eventService.subscribe(eventUrl);
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

        String eventUrl = "https://www.appdirect.com/dummyUrl";
        when(appDirectClient.getEvent(eventUrl)).thenReturn(event);

        eventService.unsubscribe(eventUrl);

        verify(userService).deleteByAccountIdentifier(anyString());
        verify(accountService).deleteAccount(anyString());
    }
    private Event getEvent(Event.EventType eventType) {
        User creator = new User();
        String uuid = "ec5d8eda-5cec-444d-9e30-125b6e4b67e2";
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
        payload.setAccount(account);
        return event;
    }

}
