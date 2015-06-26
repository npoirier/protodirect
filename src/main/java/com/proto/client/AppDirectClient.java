package com.proto.client;

import com.proto.model.event.Event;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
@Component
public class AppDirectClient {
    private static Logger LOGGER = LoggerFactory.getLogger(AppDirectClient.class);

    @Autowired
    private OAuthConsumer consumer;

    @Autowired
    private RestTemplate restTemplate;

    public Event getEvent(String url) throws OAuthException, URISyntaxException {
        String signedUrl = consumer.sign(url);
        URI uri = new URI(signedUrl);
        ResponseEntity<Event> entity = restTemplate.getForEntity(uri, Event.class);

        if (entity.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("HTTP status {} on GET {}", entity.getStatusCode(), uri);
            throw new RuntimeException("Error fetching event data.  Status: " + entity.getStatusCode() + "  URL: " + uri);
        }

        return entity.getBody();
    }
}
