package com.proto.controller;

import com.proto.ProtodirectApplication;
import com.proto.exception.UserAlreadyExistsException;
import com.proto.model.event.result.ResultBuilder;
import com.proto.service.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
@WebAppConfiguration
public class EventNotificationControllerTests {
    @InjectMocks
    EventNotificationController eventNotificationController;

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventNotificationController).build();
    }

    /**
     * When: A subscribe event notification is received and the service responds OK
     * Then: An XML response is returned with the subscription confirmation
     *
     * @throws Exception
     */
    @Test
    public void testSubscribeOK() throws Exception {
        String eventUrl = "https://www.appdirect.com/api/integration/v1/events/dummyOrder";

        when(eventService.subscribe(eventUrl)).thenReturn(
                new ResultBuilder().setSuccess(true)
                        .setAccountIdentifier("fakeco123")
                        .setMessage("Account creation successful for Fake Co. by Alice")
                        .createResult());

        mockMvc.perform(get("/notify/create?url=" + eventUrl))
                .andExpect(status().isOk())
                .andExpect(content().xml(
                        "<result>\n" +
                                "    <success>true</success>\n" +
                                "    <message>Account creation successful for Fake Co. by Alice</message>\n" +
                                "    <accountIdentifier>fakeco123</accountIdentifier>\n" +
                                "</result>"));
    }

    /**
     * When: A subscribe event notification is received and the service throws a UserAlreadyExistsException
     * Then: An XML response is returned with the error
     *
     * @throws Exception
     */
    @Test
    public void testSubscribeUserAlreadyExists() throws Exception {
        String eventUrl = "https://www.appdirect.com/api/integration/v1/events/dummyOrder";
        when(eventService.subscribe(eventUrl)).thenThrow(new UserAlreadyExistsException());

        mockMvc.perform(get("/notify/create?url=" + eventUrl))
                //   .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().xml(
                        "<result>\n" +
                                "    <success>false</success>\n" +
                                "    <errorCode>USER_ALREADY_EXISTS</errorCode>\n" +
                                "</result>"));
    }
}
