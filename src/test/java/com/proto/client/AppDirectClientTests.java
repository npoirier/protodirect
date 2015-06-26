package com.proto.client;

import com.proto.ProtodirectApplication;
import com.proto.client.AppDirectClient;
import com.proto.model.event.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
public class AppDirectClientTests {
    MockRestServiceServer mockServer;
    @Autowired
    private AppDirectClient appDirectClient;
    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        mockServer = MockRestServiceServer.createServer(restTemplate);

    }

    /**
     * When: The service gets a dummyOrder event
     * Then: The event is returned correctly
     */
    @Test
    public void testGetDummyOrder() throws Exception {
        Resource dummyResponse = new ClassPathResource("/responses/dummyOrder.xml");

        String eventUrl = "https://www.appdirect.com/api/integration/v1/events/dummyOrder";
        mockServer.expect(requestTo(startsWith(eventUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(dummyResponse, MediaType.TEXT_XML));

        Event event = appDirectClient.getEvent(eventUrl);

        mockServer.verify();
        Assert.assertEquals(Event.EventFlag.STATELESS, event.getFlag());
        Assert.assertEquals(Event.EventType.SUBSCRIPTION_ORDER, event.getType());
        Assert.assertEquals(2, event.getPayload().getOrder().getItemList().size());
    }

    /**
     * When: AppDirect responds to an event request with an http code <> OK
     * Then: A runtime exception is thrown
     */
    @Test(expected = RuntimeException.class)
    public void testGetEventError() throws Exception {
        String eventUrl = "https://www.appdirect.com/api/integration/v1/events/dummyOrder";
        mockServer.expect(requestTo(startsWith(eventUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withUnauthorizedRequest());

        Event event = appDirectClient.getEvent(eventUrl);

        mockServer.verify();
    }

    /**
     * Checks that the OAuth headers are correct when fetching an event.
     * <p>
     * Given: client key 'Dummy', secret 'secret'
     * When: Requesting the URL https://www.appdirect.com/rest/api/events/dummyChange
     * Then: We expect the requested url to be signed
     * <p>
     * GET /rest/api/events/dummyChange HTTP/1.1
     * Host: www.appdirect.com
     * Content-Type: application/xml
     * Authorization: OAuth realm="",
     * oauth_nonce="72250409",
     * oauth_timestamp="1294966759",
     * oauth_consumer_key="Dummy",
     * oauth_signature_method="HMAC-SHA1",
     * oauth_version="1.0",
     * oauth_signature="IBlWhOm3PuDwaSdxE/Qu4RKPtVE="
     */
    @Test
    public void testOAuthFetch() throws Exception {
        Resource dummyResponse = new ClassPathResource("/responses/dummyChange.xml");

        String eventUrl = "https://www.appdirect.com/api/integration/v1/events/dummyChange";
        mockServer.expect(requestTo(startsWith(eventUrl)))
                .andExpect(requestTo(containsString("oauth_signature_method=HMAC-SHA1")))
                .andExpect(requestTo(containsString("oauth_consumer_key=Dummy")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(dummyResponse, MediaType.TEXT_XML));

        appDirectClient.getEvent(eventUrl);

        mockServer.verify();
    }
}
