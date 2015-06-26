package com.proto.controller;

import com.proto.exception.UserAlreadyExistsException;
import com.proto.model.event.result.ErrorCode;
import com.proto.model.event.result.Result;
import com.proto.model.event.result.ResultBuilder;
import com.proto.service.EventService;
import oauth.signpost.exception.OAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * REST controller responsible for handling AppDirect's event notifications
 */
@RestController
public class EventNotificationController {
    private static Logger LOGGER = LoggerFactory.getLogger(EventNotificationController.class);

    @Autowired
    private EventService eventService;

    /**
     * Get the subscription status
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/notify/status")
    public Result status(@RequestParam String url) throws OAuthException, URISyntaxException {
        return eventService.status(url);
    }

    /**
     * Creates a new subscription
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/notify/create")
    public Result create(@RequestParam String url) throws UserAlreadyExistsException, OAuthException, URISyntaxException {
        return eventService.subscribe(url);
    }

    /**
     * Cancel a subscription
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/notify/cancel")
    public Result cancel(@RequestParam String url) throws OAuthException, URISyntaxException {
        return eventService.unsubscribe(url);
    }

    /**
     * Modify a subscription
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/notify/change")
    public Result change(@RequestParam String url) throws OAuthException, URISyntaxException {
        return eventService.change(url);
    }

    /**
     * Create a new user
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     * @throws UserAlreadyExistsException
     */
    @RequestMapping(value = "/notify/assign")
    public Result assign(@RequestParam String url) throws OAuthException, URISyntaxException, UserAlreadyExistsException {
        return eventService.assign(url);
    }

    /**
     * Delete a user
     *
     * @param url
     * @return
     * @throws OAuthException
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/notify/unassign")
    public Result unassign(@RequestParam String url) throws OAuthException, URISyntaxException {
        return eventService.unassign(url);
    }

    /**
     * Handles UserAlreadyExistsException to return the appropriate error code.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Result handleUserAlreadyExistsException(Exception ex) {
        return new ResultBuilder().setSuccess(false).setErrorCode(ErrorCode.USER_ALREADY_EXISTS).createResult();
    }
}
